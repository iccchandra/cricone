
package com.game.onecricket.APICallingPackage.retrofit.betlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Finished {

    @SerializedName("batting_team")
    private String battingTeam;
    @SerializedName("bet_amount")
    private String betAmount;
    @SerializedName("bet_date")
    private String betDate;
    @SerializedName("bet_time")
    private String betTime;
    @SerializedName("bet_value")
    private String betValue;
    @Expose
    private String betid;
    @SerializedName("fi_id")
    private String fiId;
    @SerializedName("home_team")
    private String homeTeam;
    @Expose
    private String id;
    @SerializedName("match_date")
    private String matchDate;
    @SerializedName("match_time")
    private String matchTime;
    @Expose
    private String matchname;
    @Expose
    private String oddname;
    @Expose
    private String oddvalue;
    @Expose
    private String status;
    @SerializedName("team_name")
    private String teamName;
    @Expose
    private String userid;
    @SerializedName("visitor_team")
    private String visitorTeam;
    @Expose
    private Object won;

    public String getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(String battingTeam) {
        this.battingTeam = battingTeam;
    }

    public String getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(String betAmount) {
        this.betAmount = betAmount;
    }

    public String getBetDate() {
        return betDate;
    }

    public void setBetDate(String betDate) {
        this.betDate = betDate;
    }

    public String getBetTime() {
        return betTime;
    }

    public void setBetTime(String betTime) {
        this.betTime = betTime;
    }

    public String getBetValue() {
        return betValue;
    }

    public void setBetValue(String betValue) {
        this.betValue = betValue;
    }

    public String getBetid() {
        return betid;
    }

    public void setBetid(String betid) {
        this.betid = betid;
    }

    public String getFiId() {
        return fiId;
    }

    public void setFiId(String fiId) {
        this.fiId = fiId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchname() {
        return matchname;
    }

    public void setMatchname(String matchname) {
        this.matchname = matchname;
    }

    public String getOddname() {
        return oddname;
    }

    public void setOddname(String oddname) {
        this.oddname = oddname;
    }

    public String getOddvalue() {
        return oddvalue;
    }

    public void setOddvalue(String oddvalue) {
        this.oddvalue = oddvalue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVisitorTeam() {
        return visitorTeam;
    }

    public void setVisitorTeam(String visitorTeam) {
        this.visitorTeam = visitorTeam;
    }

    public Object getWon() {
        return won;
    }

    public void setWon(Object won) {
        this.won = won;
    }

    @Override
    public String toString() {
        return "Finished{" +
                "battingTeam='" + battingTeam + '\'' +
                ", betAmount='" + betAmount + '\'' +
                ", betDate='" + betDate + '\'' +
                ", betTime='" + betTime + '\'' +
                ", betValue='" + betValue + '\'' +
                ", betid='" + betid + '\'' +
                ", fiId='" + fiId + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", id='" + id + '\'' +
                ", matchDate='" + matchDate + '\'' +
                ", matchTime='" + matchTime + '\'' +
                ", matchname='" + matchname + '\'' +
                ", oddname='" + oddname + '\'' +
                ", oddvalue='" + oddvalue + '\'' +
                ", status='" + status + '\'' +
                ", teamName='" + teamName + '\'' +
                ", userid='" + userid + '\'' +
                ", visitorTeam='" + visitorTeam + '\'' +
                ", won=" + won +
                '}';
    }
}
