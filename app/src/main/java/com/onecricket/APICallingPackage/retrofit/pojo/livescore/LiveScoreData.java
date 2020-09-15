
package com.onecricket.APICallingPackage.retrofit.pojo.livescore;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class LiveScoreData {

    @Expose
    private String overended;
    @Expose
    private String post;
    @Expose
    private List<Score> score;

    public String getOverended() {
        return overended;
    }

    public void setOverended(String overended) {
        this.overended = overended;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<Score> getScore() {
        return score;
    }

    public void setScore(List<Score> score) {
        this.score = score;
    }

}
