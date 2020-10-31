
package com.game.onecricket.APICallingPackage.retrofit.pojo.livescore;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LiveScroreData {

    @SerializedName("firstinnings")
    private Firstinnings mFirstinnings;
    @SerializedName("secondinnnings")
    private Secondinnings mSecondInnings;
    @SerializedName("overended")
    private String mOverended;
    @SerializedName("post")
    private String mPost;
    @SerializedName("batting_team")
    private String battingTeam;

    public Firstinnings getFirstinnings() {
        return mFirstinnings;
    }

    public void setFirstinnings(Firstinnings firstinnings) {
        mFirstinnings = firstinnings;
    }

    public String getOverended() {
        return mOverended;
    }

    public void setOverended(String overended) {
        mOverended = overended;
    }

    public String getPost() {
        return mPost;
    }

    public void setPost(String post) {
        mPost = post;
    }

    public Secondinnings getSecondinnings() {
        return mSecondInnings;
    }

    public void setSecondinnings(Secondinnings secondinnings) {
        mSecondInnings = secondinnings;
    }

    public void setBattingTeam(String battingTeam) {
        this.battingTeam = battingTeam;
    }

    public String getBattingTeam() {
        return battingTeam;
    }

    @Override
    public String toString() {
        return "LiveScroreData {" +
                "mFirstinnings=" + mFirstinnings +
                ", mSecondInnings=" + mSecondInnings +
                ", mOverended='" + mOverended + '\'' +
                ", mPost='" + mPost + '\'' +
                ", battingTeam='" + battingTeam + '\'' +
                '}';
    }
}
