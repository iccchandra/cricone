
package com.game.onecricket.APICallingPackage.retrofit.pojo.livescore;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Firstinnings {

    @SerializedName("score")
    private String mScore;
    @SerializedName("Team")
    private String mTeam;
    @SerializedName("wickets")
    private String mWickets;

    public String getScore() {
        return mScore;
    }

    public void setScore(String score) {
        mScore = score;
    }

    public String getTeam() {
        return mTeam;
    }

    public void setTeam(String team) {
        mTeam = team;
    }

    public String getWickets() {
        return mWickets;
    }

    public void setWickets(String wickets) {
        mWickets = wickets;
    }

    @Override
    public String toString() {
        return "Firstinnings{" +
                "mScore='" + mScore + '\'' +
                ", mTeam='" + mTeam + '\'' +
                ", mWickets='" + mWickets + '\'' +
                '}';
    }
}
