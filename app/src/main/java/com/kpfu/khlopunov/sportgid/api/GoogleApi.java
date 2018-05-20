package com.kpfu.khlopunov.sportgid.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hlopu on 19.05.2018.
 */

public class GoogleApi {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private GoogleRequests mGoogleRequests;
    private static GoogleApi googleApi;

    public GoogleApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
        mGoogleRequests = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(GoogleRequests.class);
    }

    public static GoogleApi getInstance() {
        if (googleApi == null) googleApi = new GoogleApi();
        return googleApi;
    }

    public GoogleRequests getmGoogleRequests() {
        return mGoogleRequests;
    }
}
