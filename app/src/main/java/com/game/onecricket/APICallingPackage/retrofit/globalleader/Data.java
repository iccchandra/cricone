
package com.game.onecricket.APICallingPackage.retrofit.globalleader;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("last_30_days")
    private List<Last30Day> last30Days;
    @SerializedName("last_7_days")
    private List<Last7Day> last7Days;
    @SerializedName("todays")
    private List<Today> todays;

    public List<Last30Day> getLast30Days() {
        return last30Days;
    }

    public void setLast30Days(List<Last30Day> last30Days) {
        this.last30Days = last30Days;
    }

    public List<Last7Day> getLast7Days() {
        return last7Days;
    }

    public void setLast7Days(List<Last7Day> last7Days) {
        this.last7Days = last7Days;
    }

    public List<Today> getTodays() {
        return todays;
    }

    public void setTodays(List<Today> todays) {
        this.todays = todays;
    }

}
