package com.game.onecricket.APICallingPackage.retrofit;

import com.game.onecricket.APICallingPackage.retrofit.betlist.SubmittedBets;
import com.game.onecricket.APICallingPackage.retrofit.livescore.LiveScoreResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    //http://3.236.20.78:4000/goalserve/live?hometeam=Gloucestershire&vistorteam=Birmingham%20Bears
    @GET("/goalserve/live")
    Observable<LiveScoreResponse> getLiveScore(@Query("hometeam") String homeTeam, @Query("vistorteam") String visitorTeam);


    @GET("/myrest/user/bet_list")
    Observable<SubmittedBets> getSubmittedBets();

    @GET("/token/update?")
    Observable<SubmitToken> submitToken(@Query("userid") String userid, @Query("mobiletoken") String mobiletoken);
}
