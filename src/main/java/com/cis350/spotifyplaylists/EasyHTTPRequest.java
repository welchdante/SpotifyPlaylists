package com.cis350.spotifyplaylists;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * Allows HTTP Requests to be easily made.
 *
 * @author Dante Welch
 * @version 1.0
 */
class EasyHTTPRequest {

    /**
     * Send a get request to a URL
     * Will send a get request to a URL and return the JSON response from it.
     * Assumes the API returns JSON.
     *
     * @param url A string for where to send the GET request.
     * @return The JSON response from the URL specified.
     */
    public JSONObject sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONObject(response.toString());
    }
}



