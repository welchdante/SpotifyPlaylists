package com.cis350.spotifyplaylists;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.AddTracksToPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

/**
 * Authenticates with a Spotify user account and builds a playlist.
 *
 * @author Dante Welch
 * @version 1.0
 */
class UserSpotifyPlaylistBuilder {

    /**
     * Client ID for the Spotify API, which is given by the project dashboard
     */
    private static final String clientId = "6ed14ff492bf439a840705e0b54e63d1";
    /**
     * Client Secret for the Spotify API, which is given by the project dashboard
     */
    private static final String clientSecret = "00245d2afffd436eab7a311317eaffe3";
    /**
     * Redirect URI used for authenticating the user account
     */
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:5000/redirect");
    /**
     * Stores the code given by the redirect URI for authenticating the user account
     */
    private static final String code = "";
    /**
     * Object used for interacting with the Spotify API
     */
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();

    /**
     * Adds a list of song objects to a playlist object
     *
     * @param userId     ID for the user's Spotify account.
     * @param playlistId ID for the playlist to add songs to.
     * @param playlist   Songs to be added to the playlist.
     */
    public static void addSongsToPlaylist(String userId, String playlistId, Set<AlbumSimplified> playlist) {
        ArrayList<String> songIds = getSongIds(playlist);
        String[] uris = buildUris(songIds);

        AddTracksToPlaylistRequest addTracksToPlaylistRequest = spotifyApi
                .addTracksToPlaylist(userId, playlistId, uris)
                .position(0)
                .build();

        try {
            final SnapshotResult snapshotResult = addTracksToPlaylistRequest.execute();
            System.out.println("Snapshot ID: " + snapshotResult.getSnapshotId());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Get track IDs for each AlbumSimplified object in the playlist
     *
     * @param playlist Set of AlbumSimplified objects that will be added to the playlist.
     * @return Array list of strings of song IDs.
     */
    private static ArrayList<String> getSongIds(Set<AlbumSimplified> playlist) {
        ArrayList<String> songIds = new ArrayList<String>();
        for (AlbumSimplified track : playlist) {
            String trackName = track.getName();
            SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(trackName)
                    .market(CountryCode.US)
                    .limit(10)
                    .offset(0)
                    .build();
            try {
                final Paging<Track> trackPaging = searchTracksRequest.execute();
                String trackId = trackPaging.getItems()[0].getId();
                songIds.add(trackId);
            } catch (IOException | SpotifyWebApiException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return songIds;
    }

    /**
     * Build the URIs for each of the song IDs that are needed for adding them to a playlist.
     *
     * @param songIds Array list of strings of song IDs.
     * @return Array of strings containing the URI of the song in the Spotify API.
     */
    private static String[] buildUris(ArrayList<String> songIds) {
        ArrayList<String> uris = new ArrayList<String>();
        String currentId;
        for (String id : songIds) {
            currentId = "spotify:track:" + id;
            uris.add(currentId);
        }
        return uris.stream().toArray(String[]::new);
    }

    /**
     * Creates the playlist.
     *
     * @param userId       User ID for the user's Spotify account.
     * @param playlistName Name of the playlist to be created.
     * @return String that is the ID for the playlist.
     */
    public static String createPlaylist(String userId, String playlistName) {
        String playlistId = "";
        CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(userId, playlistName)
                .collaborative(false)
                .public_(false)
                .description("Music to go on a roadtrip")
                .build();
        try {
            final Playlist playlist = createPlaylistRequest.execute();
            System.out.println("Name: " + playlist.getName());
            playlistId = playlist.getId();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return playlistId;
    }

    /**
     * Get the username for the Spotify account.
     *
     * @return String that is the username for a user's Spotify account.
     */
    public static String getUsername() {
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile()
                .build();
        String displayName = "";
        try {
            final User user = getCurrentUsersProfileRequest.execute();
            displayName = user.getDisplayName();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return displayName;
    }

    /**
     * Get authentication URI and set permissions for the Spotify account.
     *
     * @return String that is the authentication URI for the user's Spotify account.
     */
    public static String getAuthenticationURI() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-birthdate, user-read-email, playlist-modify-private")
                .show_dialog(true)
                .build();

        URI authenticateURI = authorizationCodeUriRequest.execute();
        return authenticateURI.toString();
    }

    /**
     * Get authentication code for Spotify account from user input.
     *
     * @return String that is the authentication code for the user account.
     */
    public static String getUserAuthenticationCode() {
        Scanner userInput = new Scanner(System.in);
        return userInput.nextLine();
    }

    /**
     * Authenticate a Spotify account using the authentication code.
     *
     * @param code Code used to authenticate the user account.
     * @return Spotify API object used to interact with the user account.
     */
    public static SpotifyApi authenticateAccount(String code) {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return spotifyApi;
    }
}
