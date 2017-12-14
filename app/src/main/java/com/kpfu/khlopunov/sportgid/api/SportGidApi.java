package com.kpfu.khlopunov.sportgid.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kpfu.khlopunov.sportgid.models.User;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hlopu on 07.11.2017.
 */

public class SportGidApi {
    private static final String BASE_URL = "http://10.17.32.141:8080/";
    private SportGidApiRequests mSportGidApiRequests;

    public SportGidApi() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
        mSportGidApiRequests = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(SportGidApiRequests.class);
    }
    public Observable<String> registration(User user){
        return mSportGidApiRequests.registrtation(user.getName(), user.getSurname(), user.getLogin(), user.getPassword());
    }
}