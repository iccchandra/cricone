
package com.onecricket.APICallingPackage.retrofit.pojo.livescore;

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

}
