
package com.onecricket.APICallingPackage.retrofit.livescore;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class CurrentOverStats {

    @Expose
    private String ballPost;
    @Expose
    private Object over;
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

    public Object getOver() {
        return over;
    }

    public void setOver(Object over) {
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
