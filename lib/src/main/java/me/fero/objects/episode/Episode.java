package me.fero.objects.episode;

import me.fero.objects.Item;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Episode extends Item {

    private final int season;
    private final int episode;
    private final String seriesId;

    public Episode(JSONObject jsonObject) throws ParseException {
        super(jsonObject);

        this.season = Integer.parseInt(jsonObject.get("Season").toString());
        this.episode = Integer.parseInt(jsonObject.get("Episode").toString());
        this.seriesId = jsonObject.get("seriesID").toString();
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getSeriesId() {
        return seriesId;
    }

}
