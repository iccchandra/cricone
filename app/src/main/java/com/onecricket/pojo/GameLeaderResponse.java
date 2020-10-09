
package com.onecricket.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class GameLeaderResponse {

    @Expose
    private List<Data> data;
    @Expose
    private String message;
    @Expose
    private String responsecode;
    @Expose
    private String status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
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

}
