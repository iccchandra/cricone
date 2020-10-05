package com.onecricket.APICallingPackage.retrofit;


import com.onecricket.APICallingPackage.Config;
import com.onecricket.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static Retrofit retrofitWithAuthorization    = null;


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
                    .baseUrl("http://13.232.85.74")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(defaultHttpClient)
                    .build();
        }
        return retrofitWithAuthorization;
    }
}
