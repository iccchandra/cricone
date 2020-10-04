
package com.onecricket.APICallingPackage.retrofit.livescore;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Bowler {

    @Expose
    private String ball;
    @Expose
    private String bowler;
    @Expose
    private String er;
    @Expose
    private String id;
    @Expose
    private String m;
    @Expose
    private String nb;
    @Expose
    private String o;
    @Expose
    private String profileid;
    @Expose
    private String r;
    @Expose
    private String w;
    @Expose
    private String wd;

    public String getBall() {
        return ball;
    }

    public void setBall(String ball) {
        this.ball = ball;
    }

    public String getBowler() {
        return bowler;
    }

    public void setBowler(String bowler) {
        this.bowler = bowler;
    }

    public String getEr() {
        return er;
    }

    public void setEr(String er) {
        this.er = er;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getNb() {
        return nb;
    }

    public void setNb(String nb) {
        this.nb = nb;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

}
