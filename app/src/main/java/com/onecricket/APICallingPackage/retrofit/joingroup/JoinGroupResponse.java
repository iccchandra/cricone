
package com.onecricket.APICallingPackage.retrofit.joingroup;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class JoinGroupResponse {

    @Expose
    private Boolean data;
    @Expose
    private String message;
    @Expose
    private String responsecode;
    @Expose
    private String status;

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
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
        return "JoinGroupResponse{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", responsecode='" + responsecode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
