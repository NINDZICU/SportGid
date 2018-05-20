package com.kpfu.khlopunov.sportgid.api;

import com.kpfu.khlopunov.sportgid.models.geocoding.Geocoding;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hlopu on 19.05.2018.
 */

public interface GoogleRequests {

    @GET("geocode/json")
    Observable<Geocoding> geocodeByAddress(@Query("address") String address, @Query("key") String key);
}
