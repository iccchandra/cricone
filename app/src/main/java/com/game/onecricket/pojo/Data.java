
package com.game.onecricket.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("bet_amount")
    private String betAmount;
    @Expose
    private String name;
    @Expose
    private String roi;
    @Expose
    private String state;
    @SerializedName("total_bet")
    private String totalBet;
    @SerializedName("total_winning")
    private String totalWinning;

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

    public void setRoi(String roi) {
        this.roi = roi;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotalBet() {
        return totalBet;
    }

    public void setTotalBet(String totalBet) {
        this.totalBet = totalBet;
    }

    public String getTotalWinning() {
        return totalWinning;
    }

    public void setTotalWinning(String totalWinning) {
        this.totalWinning = totalWinning;
    }

}
