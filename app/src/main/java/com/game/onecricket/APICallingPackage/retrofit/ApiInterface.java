package com.game.onecricket.APICallingPackage.retrofit;

import com.game.onecricket.APICallingPackage.retrofit.betlist.SubmittedBets;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.GlobalLeaderResponse;
import com.game.onecricket.APICallingPackage.retrofit.group.CreateGroupResponse;
import com.game.onecricket.APICallingPackage.retrofit.joingroup.JoinGroupResponse;
import com.game.onecricket.APICallingPackage.retrofit.state.StateResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("/myrest/user/bet_list")
    Observable<SubmittedBets> getSubmittedBets();

    @GET("/myrest/user/leaderboard")
    Observable<GlobalLeaderResponse> getGlobalLeaderBoardList();

    @POST("myrest/user/login_with_otp")
    Observable<RequestBody> login(@Body RequestBody params);

    @POST("/myrest/user/state_status")
    Observable<StateResponse> sendStateInfo(@Body RequestBody params);

    @POST("/myrest/user/create_pivate_contest")
    Observable<CreateGroupResponse> createGame(@Body RequestBody params);

    @POST("/myrest/user/join_pivate_contest")
    Observable<JoinGroupResponse> joinContest(@Body RequestBody params);
}
