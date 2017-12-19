package com.kpfu.khlopunov.sportgid.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kpfu.khlopunov.sportgid.adapters.EventAdapter;
import com.kpfu.khlopunov.sportgid.adapters.KindSportsAdapter;
import com.kpfu.khlopunov.sportgid.adapters.PlaceAdapter;
import com.kpfu.khlopunov.sportgid.api.SportGidApi;
import com.kpfu.khlopunov.sportgid.api.SportGidApiRequests;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.models.UserToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hlopu on 07.11.2017.
 */

public class ApiService {

    private Context context;
    private SportGidApi sportGidApi = SportGidApi.getInstance();
    private SportGidApiRequests requests = SportGidApi.getInstance().getmSportGidApiRequests();
    private Gson gson = new Gson();

    public ApiService(Context context) {
        this.context = context;
    }

//    public void registrationUser(String name, String surname, String login, String password) {
//        sportGidApi.registration(new User(name, surname, login, password)).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(code -> {
//                }, throwable ->
//                        Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
//    }

    public List<KindSport> getKindSports(ApiCallback callback) {
        List<KindSport> kinds = new ArrayList<>();
        requests.getKindSports().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<List<KindSport>>() {
                        }.getType();
                        List<KindSport> kindSports = gson.fromJson(apiResult.getBody().toString(), type);
                        kinds.addAll(kindSports);
                        callback.callback(kindSports);
                    }
                }, throwable -> {
                    callback.callback("ERROR");
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getKindSports " + throwable.getMessage());
                });
        return kinds;
    }

    public List<Event> getEvents(int id, String city, EventAdapter adapter) {
        List<Event> events = new ArrayList<>();
        requests.getEvents(id, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<List<Event>>() {
                        }.getType();
                        List<Event> events1 = gson.fromJson(apiResult.getBody().toString(), type);
                        events.addAll(events1);
                        adapter.setmEventList(events1);
                        adapter.notifyData();
                    }
                }, throwable -> {
                    adapter.notifyData();
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getEvents " + throwable.getMessage());
                });
        return events;
    }

    public List<Place> getPlaces(int id, String city, ApiCallback callback) {
        List<Place> places = new ArrayList<>();
        requests.getPlaces(id, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<List<Place>>() {
                        }.getType();
                        List<Place> places1 = gson.fromJson(apiResult.getBody().toString(), type);
                        places.addAll(places1);
                        callback.callback(places);
//                        adapter.setmPlaceList(places1);
//                        adapter.notifyData();
                    }
                }, throwable -> {
                    callback.callback(null);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getPlaces" + throwable.getMessage());
                });
        return places;
    }

    public void addPlace(String address, String contact, String title,
                         String description, String city, List<Integer> kindOfSport, ApiCallback callback) {
        System.out.println("CODE NURIK BESIT1111111 ");
        requests.addPlace(address, contact, title, description, city, kindOfSport).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    System.out.println("CODE NURIK BESIT " + apiResult.getCode());
                    if (apiResult.getCode() == 0) {
                        callback.callback(true);
                    }
                }, throwable -> {
                    callback.callback(false);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA addPlace" + throwable.getMessage());
                });
    }

    public void registration(String name, String surname, String email, String password, String city, ApiCallback callback) {
        requests.registration(name, surname, email, password, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<UserToken>() {
                        }.getType();
                        UserToken tokken1 = gson.fromJson(apiResult.getBody().toString(), type);
                        callback.callback(tokken1.getAccessToken());
                    } else if (apiResult.getCode() == 14) {
                        callback.callback("SAME_LOGIN");
                    }
                }, throwable -> {
                    callback.callback("ERROR");
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA registration " + throwable.getMessage());
                });
    }

    public void authentification(String email, String password, ApiCallback callback) {
        requests.authentification(email, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<UserToken>() {
                        }.getType();
                        UserToken tokken1 = gson.fromJson(apiResult.getBody().toString(), type);
                        callback.callback(tokken1.getAccessToken());
                    }
                }, throwable -> {
                    callback.callback("ERROR");
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA authentification " + throwable.getMessage());
                });
    }

    public void getUser(String access_token, ApiCallback callback) {
        requests.getUser(access_token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<User>() {
                        }.getType();
                        User user = gson.fromJson(apiResult.getBody().toString(), type);
                        callback.callback(user);
                    }
                }, throwable -> {
                    callback.callback(null);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getUser" + throwable.getMessage());
                });
    }

    public void getEvent(int id, ApiCallback callback) {
        requests.getEvent(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<Event>() {
                        }.getType();
                        Event event = gson.fromJson(apiResult.getBody().toString(), type);
                        callback.callback(event);
                    }
                }, throwable -> {
                    callback.callback(null);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getEvent" + throwable.getMessage());
                });
    }

    public void getPlace(int id, ApiCallback callback) {
        requests.getPlace(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<Place>() {
                        }.getType();
                        Place place = gson.fromJson(apiResult.getBody().toString(), type);
                        callback.callback(place);
                    }
                }, throwable -> {
                    callback.callback(null);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getPlace" + throwable.getMessage());
                });
    }

    public void addReview(int idPlace, String token, String text, int rating, ApiCallback callback) {
        requests.addReview(idPlace, token, text, rating).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        callback.callback(true);
                    }
                }, throwable -> {
                    callback.callback(false);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA addReview " + throwable.getMessage());
                });
    }

    public void updateCity(String token, String city, ApiCallback callback) {
        requests.updateCity(token, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        callback.callback(true);
                    }
                }, throwable -> {
                    callback.callback(false);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA updateReview " + throwable.getMessage());
                });
    }


}
