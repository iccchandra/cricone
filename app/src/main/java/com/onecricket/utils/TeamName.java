package com.onecricket.utils;

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
        else if (title.contains(" ")) {
            String[] titleArray = title.split(" ");
            return titleArray[0];
        }
        return title;
    }
}
