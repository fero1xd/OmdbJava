package me.fero.objects.episode;

import org.json.simple.JSONObject;

public class PartialEpisode {
    private final String title;
    private final String released;
    private final int episode;
    private final String imdbRating;
    private final String imdbId;

    public PartialEpisode(JSONObject jsonObject) {
        this.title = jsonObject.get("Title").toString();
        this.released = jsonObject.get("Released").toString();
        this.episode = Integer.parseInt(jsonObject.get("Episode").toString());
        this.imdbRating = jsonObject.get("imdbRating").toString();
        this.imdbId = jsonObject.get("imdbID").toString();
    }

    public String getTitle() {
        return title;
    }

    public String getReleased() {
        return released;
    }

    public int getEpisode() {
        return episode;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbId() {
        return imdbId;
    }
}