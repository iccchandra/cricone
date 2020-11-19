
package com.game.onecricket.APICallingPackage.retrofit.globalleader;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Today implements Serializable {

    @SerializedName("bet_amount")
    private String betAmount;
    @Expose
    private String name;
    @Expose
    private String roi;
    @Expose
    private String location;

    @SerializedName("total_winning")
    private String totalWinning;
    @SerializedName("state")
    private String state;
    @Expose
    private String userid;

    public String getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(String betAmount) {
        this.betAmount = betAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoi() {
        return roi;
    }

    public String getstate() {
        return state;
    }


    public void setstate(String state) {
        this.location = location; }

    public void setRoi(String roi) {
        this.roi = roi;
    }

    public String getTotalWinning() {
        return totalWinning;
    }

    public void setTotalWinning(String totalWinning) {
        this.totalWinning = totalWinning;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
