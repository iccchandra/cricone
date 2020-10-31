
package com.game.onecricket.APICallingPackage.retrofit.livescore;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Batsman {

    @Expose
    private String b;
    @Expose
    private String bat;
    @Expose
    private String batsman;
    @Expose
    private String dots;
    @Expose
    private String id;
    @Expose
    private String profileid;
    @Expose
    private String r;
    @Expose
    private String s4;
    @Expose
    private String s6;
    @Expose
    private String sr;
    @Expose
    private String status;

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getBat() {
        return bat;
    }

    public void setBat(String bat) {
        this.bat = bat;
    }

    public String getBatsman() {
        return batsman;
    }

    public void setBatsman(String batsman) {
        this.batsman = batsman;
    }

    public String getDots() {
        return dots;
    }

    public void setDots(String dots) {
        this.dots = dots;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    public String getS6() {
        return s6;
    }

    public void setS6(String s6) {
        this.s6 = s6;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
