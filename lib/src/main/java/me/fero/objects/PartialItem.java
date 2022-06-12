package me.fero.objects;

import me.fero.attributes.Type;
import org.json.simple.JSONObject;

public class PartialItem {
    private final String title;
    private final String year;
    private final String imdbId;
    private final Type type;
    private final String poster;

    public PartialItem(JSONObject jsonObject, Type type) {
        this.title = jsonObject.get("Title").toString();
        this.year = jsonObject.get("Year").toString();
        this.imdbId = jsonObject.get("imdbID").toString();
        this.type = type;
        this.poster = jsonObject.get("Poster").toString();
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId() {
        return imdbId;
    }

    public Type getType() {
        return type;
    }

    public String getPoster() {
        return poster;
    }
}
