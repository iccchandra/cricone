package com.onecricket.pojo;

import java.io.Serializable;

public class MatchesInfo implements Serializable {


    private String id;
    private String sportsId;
    private String time;
    private String leagueName;
    private String homeTeam;
    private String visitorsTeam;
    private String date;
    private boolean isMatchInProgress;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSportsId() {
        return sportsId;
    }

    public void setSportsId(String sportsId) {
        this.sportsId = sportsId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getVisitorsTeam() {
        return visitorsTeam;
    }

    public void setVisitorsTeam(String visitorsTeam) {
        this.visitorsTeam = visitorsTeam;
    }

    @Override
    public String toString() {
        return "MatchesInfo{" +
                "id='" + id + '\'' +
                ", sportsId='" + sportsId + '\'' +
                ", time='" + time + '\'' +
                ", leagueName='" + leagueName + '\'' +
                ", homeTeam='" + homeTeam + '\'' +
                ", visitorsTeam='" + visitorsTeam + '\'' +
                '}';
    }

    public boolean isMatchInProgress() {
        return isMatchInProgress;
    }

    public void setMatchInProgress(boolean matchInProgress) {
        isMatchInProgress = matchInProgress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
