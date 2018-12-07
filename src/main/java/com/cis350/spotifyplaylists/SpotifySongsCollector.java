package com.cis350.spotifyplaylists;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.browse.GetListOfNewReleasesRequest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Collects songs from the public Spotify API and does not use user credentials.
 *
 * @author Dante Welch
 * @version 1.0
 */
class SpotifySongsCollector {
    /**
     * Spotify API client ID.
     */
    private static final String clientId = "6ed14ff492bf439a840705e0b54e63d1";
    /**
     * Spotify API client secret key.
     */
    private static final String clientSecret = "00245d2afffd436eab7a311317eaffe3";
    /**
     * Spotify API object.
     */
    public final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();


    /**
     * Authenticates credentials to access the Spotify API.
     *
     * @param spotifyApi Spotify API object used to interact with the Spotify API
     */
    public static void authenticateCredentials(SpotifyApi spotifyApi) {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    /**
     * Gets all songs that could possibly be added to the playlist.
     *
     * @return Songs that will be added to the playlist.
     */
    public Set<AlbumSimplified> getAllSongs() {
        Set<AlbumSimplified> songs = new HashSet<>();
        GetListOfNewReleasesRequest getListOfNewReleasesRequest = spotifyApi.getListOfNewReleases()
                .country(CountryCode.US)
                .limit(50)
                .offset(0)
                .build();

        try {
            final Paging<AlbumSimplified> albumSimplifiedPaging = getListOfNewReleasesRequest.execute();

            System.out.println("Total: " + albumSimplifiedPaging.getTotal());
            for (AlbumSimplified song : albumSimplifiedPaging.getItems()) {
                songs.add(song);
            }
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return songs;
    }

    /**
     * Builds the set that contains the songs that will be added to the playlist.
     *
     * @param songs            Songs that will be added to the playlist.
     * @param playlistDuration Length of the playlist.
     * @param roadTripDuration Length of the roadtrip.
     * @return The built playlist.
     */
    public static Set<AlbumSimplified> buildPlaylist(Set<AlbumSimplified> songs, double playlistDuration, double roadTripDuration) {
        Set<AlbumSimplified> playlist = new HashSet<>();

        for (AlbumSimplified song : songs) {
            double songDuration = 210;
            if (playlistDuration < roadTripDuration + songDuration && !playlist.contains(song)) {
                playlist.add(song);
                playlistDuration += songDuration;
            }
        }

        return playlist;
    }
}






