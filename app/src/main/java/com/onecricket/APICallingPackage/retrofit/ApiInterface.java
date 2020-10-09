package com.onecricket.APICallingPackage.retrofit;

import com.onecricket.APICallingPackage.retrofit.betlist.SubmittedBets;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("/myrest/user/bet_list")
    Observable<SubmittedBets> getSubmittedBets();

    @POST("myrest/user/login_with_otp")
    Observable<RequestBody> login(@Body RequestBody params);
}
