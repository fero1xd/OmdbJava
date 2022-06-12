package me.fero.objects;

import me.fero.attributes.Type;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Series extends Item {
    private final int totalSeasons;

    public Series(JSONObject jsonObject) throws ParseException {
        super(jsonObject);
        this.type = Type.SERIES;
        this.totalSeasons = Integer.parseInt(jsonObject.get("totalSeasons").toString());
    }

    public int getTotalSeasons() { return totalSeasons; }

    @Override
    public String toString() {
        return "Series{" +
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
                ", totalSeasons=" + totalSeasons +
                '}';
    }
}
