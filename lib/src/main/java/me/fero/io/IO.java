package me.fero.io;

import me.fero.utils.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IO {

    public static Response request(String baseUrl) {
        try {
            URL url = new URL(baseUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            reader.close();
            return new Response(connection.getResponseCode(), builder.toString().trim());
        } catch (Exception e) {
            return new Response(500, "Exception caught " + e.getMessage());
        }
    }
}
