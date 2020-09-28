package com.onecricket.APICallingPackage.retrofit;

import com.onecricket.APICallingPackage.retrofit.betlist.SubmittedBets;
import com.onecricket.APICallingPackage.retrofit.pojo.livescore.LiveScroreData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    //http://3.236.20.78:4000/goalserve/live?hometeam=Gloucestershire&vistorteam=Birmingham%20Bears
    @GET("/goalserve/live")
    Observable<LiveScroreData> getLiveScore(@Query("hometeam") String homeTeam, @Query("vistorteam") String visitorTeam);


    @GET("/myrest/user/bet_list")
    Observable<SubmittedBets> getSubmittedBets();
}
