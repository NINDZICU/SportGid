package com.kpfu.khlopunov.sportgid.api;

import io.reactivex.Observable;

import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hlopu on 07.11.2017.
 */

public interface SportGidApiRequests {
    @POST("api/v1/sign_up")
    Observable<String> registrtation(@Query("name") String name, @Query("surname") String surname, @Query("email") String email, @Query("password") String password);
}
