package com.onecricket.APICallingPackage.retrofit;

import com.onecricket.APICallingPackage.retrofit.pojo.livescore.LiveScoreData;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface APIService {
    //http://3.236.20.78:7000/goalserve/live?hometeam=Hampshire&vistorteam=Kent

    @GET("http://3.236.20.78:4000/goalserve/live?hometeam=Mis%20Ainak%20Knights&vistorteam=Band-e-Amir%20Dragons")
    Observable<LiveScoreData> getLiveScore();
}
