package com.kpfu.khlopunov.sportgid.api;

import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;

import java.util.List;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hlopu on 07.11.2017.
 */

public interface SportGidApiRequests {
    @POST("api/v1/sign_up")
    Observable<String> registrtation(@Query("name") String name, @Query("surname") String surname, @Query("email") String email, @Query("password") String password);

    @GET("api/v1/sport/")
    Observable<List<KindSport>> getKindSports();

    @GET("api/v1/sport/{id}/events")
    Observable<List<Event>> getEvents(@Path("id") int kindSportId, @Query("city") String city);

    @GET("api/v1/sport/{id}/places")
    Observable<List<Place>> getPlaces(@Path("id") int kindSportId, @Query("city") String city);
}
