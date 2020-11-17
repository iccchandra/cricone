package com.game.onecricket.utils;

public class TeamName {

    public static String getFirstWord(String title) {
        if (title.toLowerCase().contains("bangalore")) {
            return "Bangalore";
        }
        else if (title.toLowerCase().contains("chennai")) {
            return "Chennai";
        }
        else if (title.toLowerCase().contains("sunrisers")) {
            return "Hyderabad";
        }
        else if (title.toLowerCase().contains("punjab")) {
            return "Punjab";
        }
        else if (title.toLowerCase().contains("rajasthan")) {
            return "Rajasthan";
        }
        else if (title.toLowerCase().contains("kolkata")) {
            return "Kolkata";
        }
        else if (title.toLowerCase().contains("delhi")) {
            return "Delhi";
        }
        else if (title.toLowerCase().contains("mumbai")) {
            return "Mumbai";
        }
        return title;
    }
}
