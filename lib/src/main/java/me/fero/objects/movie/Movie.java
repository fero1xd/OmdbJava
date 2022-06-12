package me.fero.objects.movie;

import me.fero.attributes.Type;
import me.fero.objects.Item;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Movie extends Item {

    private final String boxOffice;

    private final String website;

    public Movie(JSONObject jsonObject) throws ParseException {
        super(jsonObject);
        this.type = Type.MOVIE;
        this.boxOffice = jsonObject.get("BoxOffice").toString();
        this.website = jsonObject.get("Website").toString();
    }

    public String getBoxOffice() { return boxOffice; }


    public String getWebsite() { return website;}

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", rated='" + rated + '\'' +
                ", released='" + released + '\'' +
                ", genre=" + genre +
                ", runtime='" + runtime + '\'' +
                ", director='" + director + '\'' +
                ", writers=" + writers +
                ", actors=" + actors +
                ", languages=" + languages +
                ", country='" + country + '\'' +
                ", awards=" + awards +
                ", ratings=" + ratings +
                ", imdbRating='" + imdbRating + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", imdbVotes='" + imdbVotes + '\'' +
                ", plot='" + plot + '\'' +
                ", poster='" + poster + '\'' +
                ", type=" + type +
                ", boxOffice='" + boxOffice + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
