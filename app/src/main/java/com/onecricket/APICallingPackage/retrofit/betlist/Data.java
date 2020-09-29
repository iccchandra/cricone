
package com.onecricket.APICallingPackage.retrofit.betlist;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class Data {

    @Expose
    private List<Finished> finished;
    @Expose
    private List<InProgress> inprogress;
    @Expose
    private List<Upcoming> upcoming;

    public List<Finished> getFinished() {
        return finished;
    }

    public void setFinished(List<Finished> finished) {
        this.finished = finished;
    }

    public List<InProgress> getInprogress() {
        return inprogress;
    }

    public void setInprogress(List<InProgress> inprogress) {
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
