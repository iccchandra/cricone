
package com.game.onecricket.APICallingPackage.retrofit.livescorenew;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class CurrentOverStats {

    @Expose
    private String ballPost;
    @Expose
    private Double over;
    @Expose
    private Boolean overEnded;
    @Expose
    private String runs;

    public String getBallPost() {
        return ballPost;
    }

    public void setBallPost(String ballPost) {
        this.ballPost = ballPost;
    }

    public Double getOver() {
        return over;
    }

    public void setOver(Double over) {
        this.over = over;
    }

    public Boolean getOverEnded() {
        return overEnded;
    }

    public void setOverEnded(Boolean overEnded) {
        this.overEnded = overEnded;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

}
