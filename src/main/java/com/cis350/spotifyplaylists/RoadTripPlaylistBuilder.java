package com.cis350.spotifyplaylists;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Driver for the playlist builder.
 *
 * @author Dante Welch
 * @version 1.0
 */
class RoadTripPlaylistBuilder extends PromptHelpers {

    public static void main(String[] args) {
        asciiArt();

        /* Greet user */
        greeting();

        /* Create a playlist builder object */
        UserSpotifyPlaylistBuilder userSpotifyPlaylistBuilder = new UserSpotifyPlaylistBuilder();

        /* Create the URI for authenticating the account */
        String uri = userSpotifyPlaylistBuilder.getAuthenticationURI();

        /* Display the URI so the user can click on it */
        System.out.println("URI: " + uri);
        System.out.println();
        /* Get authentication code from user */
        System.out.println("Code: ");
        String code = userSpotifyPlaylistBuilder.getUserAuthenticationCode();

        /* Create Spotify API object */
        SpotifyApi spotifyApi = userSpotifyPlaylistBuilder.authenticateAccount(code);

        /* Get coordinates from user */
        HashMap coordinates = promptUserForCoordinates();

        /* Create object to collect from maps API */
        TomTomCollector tomTomCollector = new TomTomCollector();

        /* Build URL for the request */
        String url = tomTomCollector.buildRequestUrl((String) coordinates.get("startingLatitude"),
                (String) coordinates.get("startingLongitude"),
                (String) coordinates.get("endingLatitude"),
                (String) coordinates.get("endingLongitude"));

        System.out.println("Working some magic and getting information about your road trip...\n");

        /* Make request object */
        EasyHTTPRequest easyHTTPRequest = new EasyHTTPRequest();

        /* Create JSON object for storing info from maps API */
        JSONObject jsonObject = new JSONObject();

        /* Make request and put data into the JSON object and throws exception if it breaks. */
        try {
            jsonObject = easyHTTPRequest.sendGet(url);
//            System.out.println(jsonObject);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        /* Get length of trip */
        int travelTimeInSeconds = tomTomCollector.getTripDistance(jsonObject);
        System.out.println("Your trip will take: " + travelTimeInSeconds + " seconds.\n");

        /* Create song collector object */
        SpotifySongsCollector songCollector = new SpotifySongsCollector();

        /* Authenticate credentials for song collector */
        songCollector.authenticateCredentials(songCollector.spotifyApi);

        /* Create set to store the songs */
        Set<AlbumSimplified> songs = songCollector.getAllSongs();

        /* Create the playlist */
        Set<AlbumSimplified> playlist = songCollector.buildPlaylist(songs, 0, travelTimeInSeconds);

        /* Display the playlist to the user */
        System.out.println("Your playlist is:\n");
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        for (AlbumSimplified p : playlist) {
            System.out.println(p.getName());
        }
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");

        System.out.println("Let's add that playlist to your user account!");
        System.out.println("What is the desired name of the playlist?");
        /* Get playlist name from user */
        String playlistName = promptUserForPlaylistName();

        /* Get user's Spotify account username */
        String displayName = userSpotifyPlaylistBuilder.getUsername();

        /* Get playlist ID so we can add the songs to it */
        String playlistId = userSpotifyPlaylistBuilder.createPlaylist(displayName, playlistName);

        /* Add songs to the playlist */
        userSpotifyPlaylistBuilder.addSongsToPlaylist(displayName, playlistId, playlist);

        System.out.println("Your playlist should be on your Spotify account, enjoy!");
    }

}