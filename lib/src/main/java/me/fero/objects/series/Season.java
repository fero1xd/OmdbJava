package me.fero.objects.series;

import me.fero.Config;
import me.fero.objects.episode.PartialEpisode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class Season {
    private final String title;
    private final int season;
    private final List<PartialEpisode> episodes = new ArrayList<>();

    public Season(JSONObject jsonObject) throws ParseException {
        this.title = jsonObject.get("Title").toString();
        this.season = Integer.parseInt(jsonObject.get("Season").toString());

        JSONArray unfiltered = (JSONArray) jsonObject.get("Episodes");

        for(Object ob : unfiltered) {
            JSONObject parse = (JSONObject) Config.parser.parse(ob.toString());
            episodes.add(new PartialEpisode(parse));
        }
    }

    public String getTitle() {
        return title;
    }

    public int getSeason() {
        return season;
    }

    public List<PartialEpisode> getEpisodes() {
        return episodes;
    }
}
