package com.cis350.spotifyplaylists;

import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import static com.cis350.spotifyplaylists.SpotifySongsCollector.getAllSongs;

public class RoadTripPlaylistBuilder {

    public static void main(String[] args) {
        HashMap coordinates = promptUserForCoordinates();

        //build URL for the request
        TomTomCollector tomTomCollector = new TomTomCollector();
        String url = tomTomCollector.buildRequestUrl((String)coordinates.get("startingLatitude"),
                                                     (String)coordinates.get("startingLongitude"),
                                                     (String)coordinates.get("endingLatitude"),
                                                     (String)coordinates.get("endingLongitude"));

        //make request object and make request
        EasyHTTPRequest easyHTTPRequest = new EasyHTTPRequest();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject =  easyHTTPRequest.sendGet(url);
            System.out.println(jsonObject);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        int travelTimeInSeconds = tomTomCollector.getTripDistance(jsonObject);
        System.out.println("Your trip will take: " + travelTimeInSeconds + " seconds.");

        //build the playlist
        SpotifySongsCollector songCollector = new SpotifySongsCollector();
        songCollector.authenticateCredentials(songCollector.spotifyApi);
        Set<AlbumSimplified> songs = getAllSongs();
        Set<AlbumSimplified> playlist = songCollector.buildPlaylist(songs, 0, travelTimeInSeconds);
        System.out.println("Your playlist is:");
        for (AlbumSimplified p : playlist) {
            System.out.println(p.getName());
        }

    }

    public static HashMap promptUserForCoordinates() {
        HashMap coordinates = new HashMap();
        Scanner userInput = new Scanner(System.in);

        System.out.println("What is the starting latitude of your trip)?");
        String startingLatitude = userInput.next();
        coordinates.put("startingLatitude", startingLatitude);

        System.out.println("What is the starting longitude of your trip?");
        String startingLongitude = userInput.next();
        coordinates.put("startingLongitude", startingLongitude);

        System.out.println("What is the ending latitude of your trip?");
        String endingLatitude = userInput.next();
        coordinates.put("endingLatitude", endingLatitude);

        System.out.println("What is the ending longitude of your trip?");
        String endingLongitude = userInput.next();
        coordinates.put("endingLongitude", endingLongitude);

        userInput.close();

        return coordinates;
    }
}
