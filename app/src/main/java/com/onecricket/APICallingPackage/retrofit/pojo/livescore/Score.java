
package com.onecricket.APICallingPackage.retrofit.pojo.livescore;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Score {

    @Expose
    private Batsmanstats batsmanstats;
    @Expose
    private Bowlers bowlers;
    @Expose
    private String inningnum;
    @Expose
    private String name;
    @Expose
    private Object partnerships;
    @Expose
    private String team;
    @Expose
    private Total total;

    public Batsmanstats getBatsmanstats() {
        return batsmanstats;
    }

    public void setBatsmanstats(Batsmanstats batsmanstats) {
        this.batsmanstats = batsmanstats;
    }

    public Bowlers getBowlers() {
        return bowlers;
    }

    public void setBowlers(Bowlers bowlers) {
        this.bowlers = bowlers;
    }

    public String getInningnum() {
        return inningnum;
    }

    public void setInningnum(String inningnum) {
        this.inningnum = inningnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPartnerships() {
        return partnerships;
    }

    public void setPartnerships(Object partnerships) {
        this.partnerships = partnerships;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

}
