package com.game.onecricket.fragment;

public class UserData {
    private String    position;
    private String name;
    private String location;
    private String    points;

    public UserData(String position, String name, String location, String points) {
        this.position = position;
        this.name = name;
        this.location = location;
        this.points = points;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "position=" + position +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", points=" + points +
                '}';
    }
}
