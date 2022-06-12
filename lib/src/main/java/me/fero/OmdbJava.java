package me.fero;


import me.fero.attributes.Plot;
import me.fero.attributes.Type;
import me.fero.events.ItemFetched;
import me.fero.exceptions.ResponseError;
import me.fero.io.IO;
import me.fero.objects.episode.Episode;
import me.fero.objects.Item;
import me.fero.objects.movie.Movie;
import me.fero.objects.series.Season;
import me.fero.objects.series.Series;
import me.fero.utils.Iterator;
import me.fero.utils.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class OmdbJava {

    private String apiKey;

    /**
     * Default constructor
     */
    public OmdbJava() {}

    /**
     * Constructor to use with Key
     * @param apiKey The api key to use
     * @throws IllegalArgumentException When Api key is Either Invalid or Empty
     */
    public OmdbJava(String apiKey) throws IllegalArgumentException {
        if(apiKey.equals("")) {
            throw new IllegalArgumentException("Api key cannot be empty");
        }

        this.apiKey = apiKey;
    }


    /**
     * Set your  Api Key
     * @param apiKey The Api key to Set
     */
    public void setApiKey(String apiKey) {
        if(apiKey.equals("")) {
            throw new IllegalArgumentException("Api key cannot be empty");
        }
        this.apiKey = apiKey;
    }


    /**
     * (Synchronous) Gets a Single Movie/Series
     * @param id The imdb id
     * @param title The Title of the movie/series
     * @param plot Return short or full plot Ex Plot.FULL
     * @param type The type of item EG . Type.MOVIE
     * @param season The season number to get episodes for
     * @param episode The episode number
     * @return The ResponseItem ... Can be Series, Movie
     * @throws ResponseError if movie is not found
     */
    private Object getSingleItem(String id, String title, Plot plot, Type type, int season, int episode) throws ResponseError {
        if(this.apiKey == null || this.apiKey.equals("")) throw new ResponseError("Invalid Api Key");

        try {
            String url = Config.baseUrl + "/?apikey=" + apiKey + "&type=" + type.name().toLowerCase() + "&r=json" + "&v=1";
            if(plot != null) url += "&plot" + plot.name();
            if(title != null) url += "&t=" + URLEncoder.encode(title, "UTF-8");
            if(id != null) url += "&i=" + id;
            if(season > -1) url += "&season=" + season;
            if(episode > -1) url += "&episode=" + episode;

            Response response = IO.request(url);
            if(response.getCode() == 1) {
                throw new ResponseError(response.getResponse());
            }

            JSONObject jsonObject = (JSONObject) Config.parser.parse(response.getResponse());

            boolean responseCode = Boolean.parseBoolean(jsonObject.get("Response").toString());
            if(!responseCode) {
                throw new ResponseError("Error " + jsonObject.get("Error").toString());
            }

            if(jsonObject.get("Episodes") != null && type == Type.SEASON) {
                return new Season(jsonObject);
            }

            Type responseType = Type.findType(jsonObject.get("Type").toString());

            if(responseType == Type.MOVIE) {
                return new Movie(jsonObject);
            }

            if(responseType == Type.SERIES) {
                return new Series(jsonObject);
            }

            if(responseType == Type.EPISODE) {
                return new Episode(jsonObject);
            }

            throw new Error("Nothing Found");
        } catch (ParseException e) {
            System.out.println("|NO LOGGER| Json Response Parse Error!" + e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * (Asynchronous) Gets a Single Movie/Series
     * @param id The imdb id
     * @param title The Title of the movie/series
     * @param plot Return short or full plot Ex Plot.FULL
     * @param type The type of item EG . Type.MOVIE
     * @param handler The Class that Implements ItemFetched
     */
    private void getSingleItemAsync(String id, String title, Plot plot, Type type, int season, int episode, ItemFetched handler) {
        new Thread(() -> {
            try {
                Item singleItem = (Item) this.getSingleItem(id, title, plot, type, season, episode);
                handler.handle(singleItem);
                return;
            } catch (ResponseError e) {
                e.printStackTrace();
            }
            handler.handle(null);
        }).start();
    }

    /**
     * (Synchronous) Searches a Movie/Series
     * @param search The Search text
     * @param type The type of item EG . Type.MOVIE
     * @return An Iterator ... Can be Used to get next page and results of the current page
     */
    private Iterator search(String search, Type type) throws UnsupportedEncodingException {
        String url = Config.baseUrl + "/?apikey=" + apiKey + "&s=" +  URLEncoder.encode(search, "UTF-8") + "&type=" + type.name().toLowerCase() + "&r=json" + "&v=1";
        return new Iterator(url, type);
    }

    /**
     * Get movie by ImdbId
     * @param id The imdb id of the movie
     * @return The Movie
     */
    public Movie getMovieById(String id) throws ResponseError {
        return (Movie) getSingleItem(id, null, Plot.FULL, Type.MOVIE, -1, -1);
    }

    /**
     * Get movie by ImdbId (Asynchronous)
     * @param id The imdb id of the movie
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieById(String id, ItemFetched handler) {
        getSingleItemAsync(id,null, Plot.FULL, Type.MOVIE, -1, -1, handler);
    }

    /**
     * Get movie by ImdbId
     * @param id The imdb id of the movie
     * @param plot The plot . EG - Plot.FULL
     * @return The Movie
     */
    public Movie getMovieById(String id, Plot plot) throws ResponseError {
        return (Movie) getSingleItem(id, null, plot, Type.MOVIE, -1, -1);
    }


    /**
     * Get movie by ImdbId (Asynchronous)
     * @param id The imdb id of the movie
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieById(String id, Plot plot, ItemFetched handler) {
        getSingleItemAsync(id, null, plot, Type.MOVIE,-1, -1,  handler);
    }

    /**
     * Get movie by Title
     * @param title The title of the movie
     * @return The Movie
     */
    public Movie getMovieByTitle(String title) throws ResponseError {
        return (Movie) getSingleItem(null, title, Plot.FULL, Type.MOVIE, -1, -1);
    }

    /**
     * Get movie by Title (Asynchronous)
     * @param title The title of the movie
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieByTitle(String title, ItemFetched handler) {
        getSingleItemAsync(null, title, Plot.FULL, Type.MOVIE, -1, -1, handler);
    }


    /**
     * Get movie by Title
     * @param title The title of the movie
     * @param plot The plot . EG - Plot.FULL
     * @return The Movie
     */
    public Movie getMovieByTitle(String title, Plot plot) throws ResponseError {
        return  (Movie) getSingleItem(null, title, plot, Type.MOVIE, -1, -1);
    }

    /**
     * Get movie by Title (Asynchronous)
     * @param title The title of the movie
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieByTitle(String title, Plot plot, ItemFetched handler) {
        getSingleItemAsync(null, title, plot, Type.MOVIE,-1, -1,  handler);
    }

    /**
     * Searches a movie
     * @param search The search text to search for
     * @return An Iterator . Can be used to get Next page or current page results
     */
    public Iterator searchMovies(String search) throws UnsupportedEncodingException {
        return this.search(search, Type.MOVIE);
    }

    /**
     * Get Series by ImdbId
     * @param id The imdb id of the series
     * @return The Movie
     */
    public Series getSeriesById(String id) throws ResponseError {
        return (Series) getSingleItem(id, null, Plot.FULL, Type.SERIES, -1, -1);
    }

    /**
     * Get Series by ImdbId (Asynchronous)
     * @param id The imdb id of the series
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesById(String id, ItemFetched handler) {
        getSingleItemAsync(id, null, Plot.FULL, Type.SERIES, -1, -1, handler);
    }

    /**
     * Get Series by ImdbId
     * @param id The imdb id of the series
     * @param plot The plot . EG - Plot.FULL
     * @return The Movie
     */
    public Series getSeriesById(String id, Plot plot) throws ResponseError {
        return (Series) getSingleItem(id, null, Plot.FULL, Type.SERIES, -1, -1);
    }

    /**
     * Get Series by ImdbId (Asynchronous)
     * @param id The imdb id of the series
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesById(String id, Plot plot, ItemFetched handler) {
        getSingleItemAsync(id, null, plot, Type.SERIES,-1, -1,  handler);
    }


    /**
     * Get series by Title
     * @param title The title of the series
     * @return The Series
     */
    public Series getSeriesByTitle(String title) throws ResponseError {
        return (Series) getSingleItem(null, title, Plot.FULL, Type.SERIES, -1, -1);
    }

    /**
     * Get Series by Title (Asynchronous)
     * @param title The title of the series
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesByTitle(String title, ItemFetched handler) {
        getSingleItemAsync(null, title, Plot.FULL, Type.SERIES, -1, -1, handler);
    }

    /**
     * Get series by Title
     * @param title The title of the series
     * @param plot The plot . EG - Plot.FULL
     * @return The Series
     */
    public Series getSeriesByTitle(String title, Plot plot) throws ResponseError {
        return (Series) getSingleItem(null, title, plot, Type.SERIES, -1, -1);
    }

    /**
     * Get Series by Title (Asynchronous)
     * @param title The title of the series
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesByTitle(String title, Plot plot, ItemFetched handler) {
        getSingleItemAsync(null, title, plot, Type.SERIES,-1, -1,  handler);
    }

    /**
     * Searches a series
     * @param search The search text to search for
     * @return An Iterator . Can be used to get Next page or current page results
     */
    public Iterator searchSeries(String search) throws UnsupportedEncodingException {
        return this.search(search, Type.SERIES);
    }

    /**
     * Gets Season of a series
     * @param id The imdb id of the series
     * @param season The season of the series
     * @return A season object containing Partial Episodes
     */
    public Season getSeasonById(String id, int season) throws ResponseError {
        return (Season) this.getSingleItem(id, null, null, Type.SEASON, season, -1);
    }


    /**
     * Gets Season of a series
     * @param title The title  of the series
     * @param season The season of the series
     * @return A season object containing Partial Episodes
     */
    public Season getSeasonByTitle(String title, int season) throws ResponseError {
        return (Season) this.getSingleItem(null, title, null, Type.SEASON, season, -1);
    }


    /**
     * Gets a single episode by id
     * @param id The id of the episode
     * @return A episode object
     */
    public Episode getEpisodeById(String id) throws ResponseError {
        return (Episode) this.getSingleItem(id, null, null, Type.EPISODE, -1, -1);
    }
}
