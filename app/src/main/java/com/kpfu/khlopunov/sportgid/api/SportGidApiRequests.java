package com.kpfu.khlopunov.sportgid.api;

import com.kpfu.khlopunov.sportgid.models.ApiResult;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.models.UserToken;

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

    @GET("api/v1/sport/")
    Observable<ApiResult<List<KindSport>>> getKindSports();

    @GET("api/v1/sport/{id}/events")
    Observable<ApiResult<List<Event>>> getEvents(@Path("id") int kindSportId, @Query("city") String city);

    @GET("api/v1/sport/{id}/places")
    Observable<ApiResult<List<Place>>> getPlaces(@Path("id") int kindSportId, @Query("city") String city);

    @POST("api/v1/place/add")
    Observable<ApiResult> addPlace(@Query("address") String address, @Query("contact") String contact, @Query("title") String title,
                                   @Query("description") String description, @Query("city") String city, @Query("photo") String photo,
                                   @Query("sport") List<Integer> kindOfSport, @Query("token") String token);

    @POST("api/v1/event/add")
    Observable<ApiResult> addEvent(@Query("title") String title, @Query("description") String description,
                                   @Query("maxOfMembers") int maxOfMembers, @Query("price") String price, @Query("token") String token,
                                   @Query("photo") String photo, @Query("sport") String sport, @Query("place") Place place);

    @POST("api/v1/sign_up")
    Observable<ApiResult<UserToken>> registration(@Query("name") String name, @Query("surname") String surname, @Query("email") String email, @Query("password") String password,
                                                  @Query("city") String city);

    @POST("api/v1/sign_in")
    Observable<ApiResult<UserToken>> authentification(@Query("login") String login, @Query("password") String password);

    @POST("api/v1/place/{id}/review")
    Observable<ApiResult> addReview(@Path("id") int idPlace, @Query("token") String token,
                                    @Query("body") String text, @Query("rating") int rating);

    @POST("api/v1/profile")
    Observable<ApiResult> updateCity(@Query("token") String token, @Query("city") String city);


    @GET("api/v1/profile")
    Observable<ApiResult<User>> getUser(@Query("token") String token);

    @GET("api/v1/event/{id}")
    Observable<ApiResult<Event>> getEvent(@Path("id") int id);

    @GET("api/v1/place/{id}")
    Observable<ApiResult<Place>> getPlace(@Path("id") int id);

    @POST("api/v1/event/{id}/delete")
    Observable<ApiResult> deleteEvent(@Path("id") int id, @Query("token") String token);

    @POST("api/v1/place/{id}/delete")
    Observable<ApiResult> deletePlace(@Path("id") int id, @Query("token") String token);

    @POST("api/v1/place/{id}/complaint")
    Observable<ApiResult> sendComplaint(@Path("id") int id, @Query("token") String token,
                                        @Query("title") String title, @Query("body") String body);
}
