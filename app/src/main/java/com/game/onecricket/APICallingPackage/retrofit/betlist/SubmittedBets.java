
package com.game.onecricket.APICallingPackage.retrofit.betlist;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class SubmittedBets {

    @Expose
    private Data data;
    @Expose
    private String message;
    @Expose
    private String responsecode;
    @Expose
    private String status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubmittedBets{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", responsecode='" + responsecode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
