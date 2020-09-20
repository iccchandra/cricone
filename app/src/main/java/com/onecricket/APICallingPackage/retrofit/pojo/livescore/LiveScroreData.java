
package com.onecricket.APICallingPackage.retrofit.pojo.livescore;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LiveScroreData {

    @SerializedName("firstinnings")
    private Firstinnings mFirstinnings;
    @SerializedName("Sirstinnings")
    private Secondinnings mSecondInnings;
    @SerializedName("overended")
    private String mOverended;
    @SerializedName("post")
    private String mPost;

    public Firstinnings getFirstinnings() {
        return mFirstinnings;
    }

    public void setFirstinnings(Firstinnings firstinnings) {
        mFirstinnings = firstinnings;
    }

    public String getOverended() {
        return mOverended;
    }

    public void setOverended(String overended) {
        mOverended = overended;
    }

    public String getPost() {
        return mPost;
    }

    public void setPost(String post) {
        mPost = post;
    }

    public Secondinnings getSecondinnings() {
        return mSecondInnings;
    }

    public void setSecondinnings(Secondinnings secondinnings) {
        mSecondInnings = secondinnings;
    }


}
