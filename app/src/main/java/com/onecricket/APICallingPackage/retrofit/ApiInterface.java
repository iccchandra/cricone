package com.onecricket.APICallingPackage.retrofit;

import com.onecricket.APICallingPackage.retrofit.betlist.SubmittedBets;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("/myrest/user/bet_list")
    Observable<SubmittedBets> getSubmittedBets();
}
