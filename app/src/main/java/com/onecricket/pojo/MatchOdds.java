package com.onecricket.pojo;

public class MatchOdds {

    private String id;
    private String odds;
    private String name;
    private boolean isSelected;
    private int betAmount;
    private int previousBetAmount;
    private float returnAmount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MatchOdds{" +
                "id='" + id + '\'' +
                ", odds='" + odds + '\'' +
                ", name='" + name + '\'' +
                ", isSelected=" + isSelected +
                ", betAmount=" + betAmount +
                ", returnAmount=" + returnAmount +
                '}';
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public float getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(float returnAmount) {
        this.returnAmount = returnAmount;
    }

    public float getPreviousBetAmount() {
        return previousBetAmount;
    }

    public void setPreviousBetAmount(int previousBetAmount) {
        this.previousBetAmount = previousBetAmount;
    }
}
