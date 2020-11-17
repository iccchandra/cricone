package com.game.onecricket.APICallingPackage.retrofit;


import com.game.onecricket.BuildConfig;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static Retrofit retrofitWithAuthorization    = null;
//    public static final String BASE_URL = "http://13.232.85.74";
    public static final String BASE_URL = "http://api.1cricket.in";


    public static Retrofit getClientWithAuthorisation(String token) {
        if (retrofitWithAuthorization == null) {
            // Define the interceptor, add authentication headers
            Interceptor interceptor = chain -> {
                Request newRequest = chain.request().newBuilder()
                                          .addHeader("Content-Type", "application/json")
                                          .addHeader("Authorization", token)
                                          .build();
                return chain.proceed(newRequest);
            };

            HttpLoggingInterceptor interceptorLog = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                interceptorLog.level(HttpLoggingInterceptor.Level.BODY);
            } else {
                interceptorLog.level(HttpLoggingInterceptor.Level.NONE);
            }

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            OkHttpClient defaultHttpClient = builder
                    .addInterceptor(interceptorLog)
                    .addInterceptor(interceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofitWithAuthorization = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(defaultHttpClient)
                    .build();
        }
        return retrofitWithAuthorization;
    }

    public static RequestBody getRequestBody(JSONObject objt) {
        return RequestBody.create(objt.toString(), okhttp3.MediaType.parse("application/json; charset=utf-8") );
    }
}
