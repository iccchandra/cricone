
package com.onecricket.APICallingPackage.retrofit.livescorenew;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class LiveScoreResponse {

    @Expose
    private String battingTeam;
    @Expose
    private String goalId;
    @Expose
    private String homeTeam;
    @Expose
    private Boolean inProgress;
    @Expose
    private Long lastOver;
    @Expose
    private String lastOverScore;
    @Expose
    private String matchDate;
    @Expose
    private String matchId;
    @Expose
    private String matchType;
    @Expose
    private Scores scores;
    @Expose
    private String visitorTeam;

    public String getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(String battingTeam) {
        this.battingTeam = battingTeam;
    }

    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Boolean getInProgress() {
        return inProgress;
    }

    public void setInProgress(Boolean inProgress) {
        this.inProgress = inProgress;
    }

    public Long getLastOver() {
        return lastOver;
    }

    public void setLastOver(Long lastOver) {
        this.lastOver = lastOver;
    }

    public String getLastOverScore() {
        return lastOverScore;
    }

    public void setLastOverScore(String lastOverScore) {
        this.lastOverScore = lastOverScore;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public Scores getScores() {
        return scores;
    }

    public void setScores(Scores scores) {
        this.scores = scores;
    }

    public String getVisitorTeam() {
        return visitorTeam;
    }

    public void setVisitorTeam(String visitorTeam) {
        this.visitorTeam = visitorTeam;
    }

}
