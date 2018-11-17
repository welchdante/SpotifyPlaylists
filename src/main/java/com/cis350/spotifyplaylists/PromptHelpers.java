package com.cis350.spotifyplaylists;

import java.util.HashMap;
import java.util.Scanner;

/** User prompts to be used to gather data from the user.
 * @author Dante Welch
 * @version 1.0
 */
public class PromptHelpers {

    /** Greets the user and gives directions of how to use the program.
     */
    public static void greeting() {
        System.out.println("Hello! Let's make a playlist for your roadtrip. \n" +
                "First, we need to authenticate with your Spotify account.\n" +
                "Click the link provided, log in to your account, then the url you are redirected to should\n" +
                "have a code in it. Copy and paste that code into the program to authenticate your account.");
    }

    /** Prompts the user for their desired start and end locations.
     * @return Hash map of the starting and ending latitute and longitude of the road trip.
     * */
    public static HashMap promptUserForCoordinates() {
        Scanner userInput = new Scanner(System.in);
        HashMap coordinates = new HashMap();

        coordinates = getStartingLatitude(userInput, coordinates);
        coordinates = getStartingLongitude(userInput, coordinates);
        coordinates = getEndingLatitude(userInput, coordinates);
        coordinates = getEndingLongitude(userInput, coordinates);

        return coordinates;
    }

    /**
     * @param userInput Scanner object for user input.
     * @param coordinates Stores coordinates.
     * @return Current coordinates given by user.
     */
    public static HashMap getStartingLatitude(Scanner userInput, HashMap coordinates) {
        System.out.println("What is the starting latitude of your trip)?");
        String startingLatitude = userInput.next();
        coordinates.put("startingLatitude", startingLatitude);
        return coordinates;
    }

    /**
     * @param userInput Scanner object for user input.
     * @param coordinates Stores coordinates.
     * @return Current coordinates given by user.
     */
    public static HashMap getStartingLongitude(Scanner userInput, HashMap coordinates) {
        System.out.println("What is the starting longitude of your trip?");
        String startingLongitude = userInput.next();
        coordinates.put("startingLongitude", startingLongitude);
        return coordinates;
    }

    /**
     * @param userInput Scanner object for user input.
     * @param coordinates Stores coordinates.
     * @return Current coordinates given by user.
     */
    public static HashMap getEndingLatitude(Scanner userInput, HashMap coordinates) {
        System.out.println("What is the ending latitude of your trip?");
        String endingLatitude = userInput.next();
        coordinates.put("endingLatitude", endingLatitude);
        return coordinates;
    }

    /**
     * @param userInput Scanner object for user input.
     * @param coordinates Stores coordinates.
     * @return Current coordinates given by user.
     */
    public static HashMap getEndingLongitude(Scanner userInput, HashMap coordinates) {
        System.out.println("What is the ending longitude of your trip?");
        String endingLongitude = userInput.next();
        coordinates.put("endingLongitude", endingLongitude);
        return coordinates;
    }

    /** Prompts the user for the desired name of the playlist.
     * @return String of the name of the playlist
     * */
    public static String prompUserForPlaylistName() {
        Scanner userInput = new Scanner(System.in);
        String playlistName = userInput.next();
        return playlistName;
    }
}
