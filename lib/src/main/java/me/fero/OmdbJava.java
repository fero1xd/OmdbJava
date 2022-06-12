package me.fero;


import me.fero.attributes.Plot;
import me.fero.attributes.Type;
import me.fero.events.ItemFetched;
import me.fero.exceptions.ResponseError;
import me.fero.io.IO;
import me.fero.objects.Item;
import me.fero.objects.Movie;
import me.fero.objects.Series;
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
     * @return The ResponseItem ... Can be Series, Movie
     * @throws ResponseError if movie is not found
     */
    private Item getSingleItem(String id, String title, Plot plot, Type type) throws ResponseError {
        if(this.apiKey == null || this.apiKey.equals("")) throw new ResponseError("Invalid Api Key");

        try {
            String url = Config.baseUrl + "/?apikey=" + apiKey + "&type=" + type.name().toLowerCase() + "&plot=" + plot.name() + "&r=json" + "&v=1";

            if(title != null) url += "&t=" + URLEncoder.encode(title, "UTF-8");
            if(id != null) url += "&i=" + id;

            Response response = IO.request(url);
            if(response.getCode() == 1) {
                throw new ResponseError(response.getResponse());
            }

            JSONObject jsonObject = (JSONObject) Config.parser.parse(response.getResponse());

            boolean responseCode = Boolean.parseBoolean(jsonObject.get("Response").toString());
            if(!responseCode) {

                throw new ResponseError("Error " + jsonObject.get("Error").toString());
            }

            Type responseType = Type.findType(jsonObject.get("Type").toString());

            if(responseType == Type.MOVIE) {
                return new Movie(jsonObject);
            }

            if(responseType == Type.SERIES) {
                return new Series(jsonObject);
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
    private void getSingleItemAsync(String id, String title, Plot plot, Type type, ItemFetched handler) {
        new Thread(() -> {
            try {
                Item singleItem = this.getSingleItem(id, title, plot, type);
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
        return (Movie) getSingleItem(id, null, Plot.FULL, Type.MOVIE);
    }

    /**
     * Get movie by ImdbId (Asynchronous)
     * @param id The imdb id of the movie
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieById(String id, ItemFetched handler) {
        getSingleItemAsync(id,null, Plot.FULL, Type.MOVIE, handler);
    }

    /**
     * Get movie by ImdbId
     * @param id The imdb id of the movie
     * @param plot The plot . EG - Plot.FULL
     * @return The Movie
     */
    public Movie getMovieById(String id, Plot plot) throws ResponseError {
        return (Movie) getSingleItem(id, null, plot, Type.MOVIE);
    }


    /**
     * Get movie by ImdbId (Asynchronous)
     * @param id The imdb id of the movie
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieById(String id, Plot plot, ItemFetched handler) {
        getSingleItemAsync(id, null, plot, Type.MOVIE, handler);
    }

    /**
     * Get movie by Title
     * @param title The title of the movie
     * @return The Movie
     */
    public Movie getMovieByTitle(String title) throws ResponseError {
        return (Movie) getSingleItem(null, title, Plot.FULL, Type.MOVIE);
    }

    /**
     * Get movie by Title (Asynchronous)
     * @param title The title of the movie
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieByTitle(String title, ItemFetched handler) {
        getSingleItemAsync(null, title, Plot.FULL, Type.MOVIE, handler);
    }


    /**
     * Get movie by Title
     * @param title The title of the movie
     * @param plot The plot . EG - Plot.FULL
     * @return The Movie
     */
    public Movie getMovieByTitle(String title, Plot plot) throws ResponseError {
        return  (Movie) getSingleItem(null, title, plot, Type.MOVIE);
    }

    /**
     * Get movie by Title (Asynchronous)
     * @param title The title of the movie
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getMovieByTitle(String title, Plot plot, ItemFetched handler) {
        getSingleItemAsync(null, title, plot, Type.MOVIE, handler);
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
        return (Series) getSingleItem(id, null, Plot.FULL, Type.SERIES);
    }

    /**
     * Get Series by ImdbId (Asynchronous)
     * @param id The imdb id of the series
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesById(String id, ItemFetched handler) {
        getSingleItemAsync(id, null, Plot.FULL, Type.SERIES, handler);
    }

    /**
     * Get Series by ImdbId
     * @param id The imdb id of the series
     * @param plot The plot . EG - Plot.FULL
     * @return The Movie
     */
    public Series getSeriesById(String id, Plot plot) throws ResponseError {
        return (Series) getSingleItem(id, null, Plot.FULL, Type.SERIES);
    }

    /**
     * Get Series by ImdbId (Asynchronous)
     * @param id The imdb id of the series
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesById(String id, Plot plot, ItemFetched handler) {
        getSingleItemAsync(id, null, plot, Type.SERIES, handler);
    }


    /**
     * Get series by Title
     * @param title The title of the series
     * @return The Series
     */
    public Series getSeriesByTitle(String title) throws ResponseError {
        return (Series) getSingleItem(null, title, Plot.FULL, Type.SERIES);
    }

    /**
     * Get Series by Title (Asynchronous)
     * @param title The title of the series
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesByTitle(String title, ItemFetched handler) {
        getSingleItemAsync(null, title, Plot.FULL, Type.SERIES, handler);
    }

    /**
     * Get series by Title
     * @param title The title of the series
     * @param plot The plot . EG - Plot.FULL
     * @return The Series
     */
    public Series getSeriesByTitle(String title, Plot plot) throws ResponseError {
        return (Series) getSingleItem(null, title, plot, Type.SERIES);
    }

    /**
     * Get Series by Title (Asynchronous)
     * @param title The title of the series
     * @param plot The plot . EG - Plot.FULL
     * @param handler The Class that Implements ItemFetched
     */
    public void getSeriesByTitle(String title, Plot plot, ItemFetched handler) {
        getSingleItemAsync(null, title, plot, Type.SERIES, handler);
    }

    /**
     * Searches a series
     * @param search The search text to search for
     * @return An Iterator . Can be used to get Next page or current page results
     */
    public Iterator searchSeries(String search) throws UnsupportedEncodingException {
        return this.search(search, Type.SERIES);
    }
}
