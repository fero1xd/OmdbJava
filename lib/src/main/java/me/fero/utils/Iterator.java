package me.fero.utils;


import me.fero.Config;
import me.fero.attributes.Type;
import me.fero.io.IO;
import me.fero.objects.PartialItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class Iterator {

    private int page = 1;
    private String url;
    private int totalResults;
    private Type type;


    public Iterator(String url, Type type) {
        this.url = url;
        this.type = type;
    }

    /**
     * Get Result on Current Page
     * Automatically increments page
     * @return List of partial found items . Will return null of no results left
     */
    @Nullable
    public List<PartialItem> getNextPage() {
        try {
            String clone = url + "&page=" + page;
            Response response = IO.request(clone);
            JSONObject jsonObject = (JSONObject) Config.parser.parse(response.getResponse());

            boolean responseCode = Boolean.parseBoolean(jsonObject.get("Response").toString());
            if(!responseCode) {
                return null;
            }

            totalResults = Integer.parseInt(jsonObject.get("totalResults").toString());


            JSONArray search = (JSONArray) jsonObject.get("Search");
            List<PartialItem> result = new ArrayList<>();

            for(Object ob : search) {
                JSONObject parse = (JSONObject) Config.parser.parse(ob.toString());
                result.add(new PartialItem(parse, this.type));
            }

            page += 1;
            return result;
        } catch (ParseException e) {
            System.out.println("|NO LOGGER| Json Response Parse Error!" + e);
        }
        return null;
    }

    /**
     * Helper for getting current page
     */
    public int getPage() { return page; }


    /**
     * Helper for getting total results
     */
    public int getTotalResults() { return totalResults; }
}
