package me.fero.objects;

import me.fero.Config;
import me.fero.attributes.Rating;
import me.fero.attributes.Type;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item  {
    protected final String title;
    protected final String year;
    protected final String rated;
    protected final String released;
    protected final List<String> genre;
    protected final String runtime;
    protected final String director;
    protected final List<String> writers;
    protected final List<String> actors;
    protected final List<String> languages;

    protected final String country;
    protected final List<String> awards;
    protected final List<Rating> ratings = new ArrayList<>();
    protected final String imdbRating;

    protected final String imdbId;
    protected final String imdbVotes;

    protected final String plot;

    protected final String poster;
    protected Type type;


    public Item(JSONObject jsonObject) throws ParseException {
        title = jsonObject.get("Title").toString();
        year = jsonObject.get("Year").toString();
        rated = jsonObject.get("Rated").toString();
        released = jsonObject.get("Released").toString();
        runtime = jsonObject.get("Runtime").toString();
        genre = Arrays.asList(jsonObject.get("Genre").toString().trim().split("[,]"));
        director = jsonObject.get("Director").toString();
        writers = Arrays.asList(jsonObject.get("Writer").toString().trim().split("[,]"));
        actors =  Arrays.asList(jsonObject.get("Actors").toString().trim().split("[,]"));
        plot = jsonObject.get("Plot").toString();

        languages = Arrays.asList(jsonObject.get("Language").toString().trim().split("[,]"));
        country = jsonObject.get("Country").toString();
        awards = Arrays.asList(jsonObject.get("Language").toString().trim().split("[&]"));

        JSONArray unfiltered = (JSONArray) jsonObject.get("Ratings");


        for(Object ob : unfiltered) {
            JSONObject parse = (JSONObject) Config.parser.parse(ob.toString());

            String value = parse.get("Value").toString().trim();
            ratings.add(new Rating(parse.get("Source").toString(), value));
        }

        imdbId = jsonObject.get("imdbID").toString();

        imdbRating = jsonObject.get("imdbRating").toString();
        imdbVotes = jsonObject.get("imdbVotes").toString();
        poster = jsonObject.get("Poster").toString();
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public List<String> getGenre() {
        return genre;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getDirector() {
        return director;
    }

    public List<String> getWriters() {
        return writers;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getCountry() {
        return country;
    }

    public List<String> getAwards() {
        return awards;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public String getImdbRating() { return imdbRating;}

    public String getImdbId() {
        return imdbId;
    }

    public String getPlot() {
        return plot;
    }

    public String getPoster() {
        return poster;
    }

    public Type getType() {
        return type;
    }

    public String getImdbVotes() { return imdbVotes; }

}
