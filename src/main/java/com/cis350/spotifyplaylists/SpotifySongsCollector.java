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

public class SpotifySongsCollector {
    private static final String clientId = "6ed14ff492bf439a840705e0b54e63d1";
    private static final String clientSecret = "00245d2afffd436eab7a311317eaffe3";

    private static SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();

    public static void main(String[] args) {
        double roadTripDuration = 4000;
        double playlistDuration = 0;
        authenticateCredentials(spotifyApi);
        Set<AlbumSimplified> songs = getAllSongs();
        Set<AlbumSimplified> playlist = buildPlaylist(songs, playlistDuration, roadTripDuration);
        System.out.println("Your playlist is:");
        for (AlbumSimplified p : playlist) {
            System.out.println(p.getName());
        }
    }

    public static void authenticateCredentials(SpotifyApi spotifyApi) {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();

        //authenticate credentials
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static Set<AlbumSimplified> getAllSongs() {
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






