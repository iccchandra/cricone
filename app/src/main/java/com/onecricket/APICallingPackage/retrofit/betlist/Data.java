
package com.onecricket.APICallingPackage.retrofit.betlist;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Data {

    @Expose
    private List<Object> finished;
    @Expose
    private List<Object> inprogress;
    @Expose
    private List<Upcoming> upcoming;

    public List<Object> getFinished() {
        return finished;
    }

    public void setFinished(List<Object> finished) {
        this.finished = finished;
    }

    public List<Object> getInprogress() {
        return inprogress;
    }

    public void setInprogress(List<Object> inprogress) {
        this.inprogress = inprogress;
    }

    public List<Upcoming> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<Upcoming> upcoming) {
        this.upcoming = upcoming;
    }

    @Override
    public String toString() {
        return "Data{" +
                "finished=" + finished +
                ", inprogress=" + inprogress +
                ", upcoming=" + upcoming +
                '}';
    }
}
