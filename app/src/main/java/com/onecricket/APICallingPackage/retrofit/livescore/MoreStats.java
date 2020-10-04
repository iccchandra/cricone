
package com.onecricket.APICallingPackage.retrofit.livescore;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class MoreStats {

    @Expose
    private CurrentOverStats currentOverStats;
    @Expose
    private Firstinnings firstinnings;
    @Expose
    private String post;
    @Expose
    private Secondinnnings secondinnnings;

    public CurrentOverStats getCurrentOverStats() {
        return currentOverStats;
    }

    public void setCurrentOverStats(CurrentOverStats currentOverStats) {
        this.currentOverStats = currentOverStats;
    }

    public Firstinnings getFirstinnings() {
        return firstinnings;
    }

    public void setFirstinnings(Firstinnings firstinnings) {
        this.firstinnings = firstinnings;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Secondinnnings getSecondinnnings() {
        return secondinnnings;
    }

    public void setSecondinnnings(Secondinnnings secondinnnings) {
        this.secondinnnings = secondinnnings;
    }

}
