
package com.game.onecricket.APICallingPackage.retrofit.livescorenew;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class OverallScores {

    @Expose
    private String b;
    @Expose
    private String ext;
    @Expose
    private String lb;
    @Expose
    private String nb;
    @Expose
    private String p;
    @Expose
    private String rr;
    @Expose
    private String tot;
    @Expose
    private String wd;
    @Expose
    private String wickets;

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getNb() {
        return nb;
    }

    public void setNb(String nb) {
        this.nb = nb;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }

    public String getTot() {
        return tot;
    }

    public void setTot(String tot) {
        this.tot = tot;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getWickets() {
        return wickets;
    }

    public void setWickets(String wickets) {
        this.wickets = wickets;
    }

}
