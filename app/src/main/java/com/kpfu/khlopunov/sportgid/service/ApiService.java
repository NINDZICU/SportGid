package com.kpfu.khlopunov.sportgid.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.kpfu.khlopunov.sportgid.adapters.EventAdapter;
import com.kpfu.khlopunov.sportgid.adapters.KindSportsAdapter;
import com.kpfu.khlopunov.sportgid.adapters.MyEventsAdapter;
import com.kpfu.khlopunov.sportgid.adapters.PlaceAdapter;
import com.kpfu.khlopunov.sportgid.api.SportGidApi;
import com.kpfu.khlopunov.sportgid.api.SportGidApiRequests;
import com.kpfu.khlopunov.sportgid.fragments.ApiCallback;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.User;
import com.kpfu.khlopunov.sportgid.models.UserToken;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
                        kinds.addAll(apiResult.getBody());
                        callback.callback(apiResult.getBody());
                    }
                }, throwable -> {
                    callback.callback("ERROR");
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getKindSports " + throwable.getMessage());
                });
        return kinds;
    }

    public void searchKindSports(ApiCallback callback, String query) {
        requests.searchKindSport(query).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        callback.callback(apiResult.getBody());
                    }
                }, throwable -> {
                    callback.callback("ERROR");
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getKindSports " + throwable.getMessage());
                });
    }

    public void getEventsByPrice(EventAdapter adapter, int kindSportId, String priceMin, String priceMax) {
        requests.getFilterEventByPrice(kindSportId, priceMin, priceMax).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        adapter.setmEventList(apiResult.getBody());
                        adapter.notifyData();
                    }
                }, throwable -> {
                    adapter.notifyData();
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getFilterKindSports " + throwable.getMessage());
                });
    }

    public List<Event> getEvents(int id, String city, EventAdapter adapter) {
        List<Event> events = new ArrayList<>();
        requests.getEvents(id, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
//                        Type type = new TypeToken<List<Event>>() {
//                        }.getType();
//                        List<Event> events1 = gson.fromJson(apiResult.getBody().toString(), type);
                        events.addAll(apiResult.getBody());
                        adapter.setmEventList(apiResult.getBody());
                        adapter.notifyData();
                    }
                }, throwable -> {
                    adapter.notifyData();
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getEvents " + throwable.getMessage());
                });
        return events;
    }


    public void getMyEvents(String token, ApiCallback callback) {
        List<Event> events = Collections.emptyList();
        requests.getMyEvents(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        events.addAll(apiResult.getBody().getEvents());
                        callback.callback(events);
                    }
                }, throwable -> {
                    callback.callback(null);
                    Log.d("THROW getMyEvents", throwable.getMessage());
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void getMyPlaces(String token, ApiCallback callback) {
        System.out.println("AAAAA " + token);
        List<Place> places = new ArrayList<>();
        System.out.println("MYPLACES " + places.size());
        requests.getMyPlaces(token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        places.addAll(apiResult.getBody().getPlaces());
                        callback.callback(places);
                    }
                }, throwable -> {
                    callback.callback(null);
                    Log.d("THROW getMyPlaces", throwable.getMessage());
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void subscribeEvent(int idEvent, String token, ApiCallback apiCallback) {
        requests.subscribe(idEvent, token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        apiCallback.callback(true);
                    }
                }, throwable -> {
                    apiCallback.callback(null);
                    Log.d("THROW OT NUR getPlaces", throwable.getMessage());
                    Toast.makeText(context, "Throw subscribeEvent " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void unsubscribeEvent(int idEvent, String token, ApiCallback apiCallback) {
        requests.unsubscribe(idEvent, token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        apiCallback.callback(false);
                    }
                }, throwable -> {
                    apiCallback.callback(null);
                    Log.d("THROW OT NUR getPlaces", throwable.getMessage());
                    Toast.makeText(context, "Throw unsubscribeEvent " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public List<Place> getPlaces(int id, String city, ApiCallback callback) {
        List<Place> places = new ArrayList<>();
        requests.getPlaces(id, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    Log.d("GET PLACES CODE", String.valueOf(apiResult.getCode()));
                    if (apiResult.getCode() == 0) {
                        places.addAll(apiResult.getBody());
                        callback.callback(places);
//                        adapter.setmPlaceList(places1);
//                        adapter.notifyData();
                    }
                }, throwable -> {
                    callback.callback(null);
                    Log.d("THROW OT NUR getPlaces", throwable.getMessage());
                    Toast.makeText(context, "Throw getPlaces " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
        return places;
    }

    public void addPlace(String address, String contact, String title,
                         String description, String city, String photo, List<Integer> kindOfSport, String token,
                         ApiCallback callback) {
        requests.addPlace(address, contact, title, description, city, photo, kindOfSport, token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    System.out.println("CODE add Place " + apiResult.getCode());
                    if (apiResult.getCode() == 0) {
                        callback.callback(true);
                    }
                }, throwable -> {
                    callback.callback(false);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA addPlace" + throwable.getMessage());
                });
    }

    public void addEvent(String title, String description, int maxOfMembers, String price, String token,
                         String photo, String sport, Place place, double x, double y, ApiCallback apiCallback) {
        requests.addEvent(title, description, maxOfMembers, price, token, photo, sport, place, x, y)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        apiCallback.callback(true);
                    }
                }, throwable -> {
                    apiCallback.callback(false);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Throw add Event ", throwable.getMessage());
                });
    }

    public void deletePlace(int idPlace, String token, ApiCallback apiCallback) {
        requests.deletePlace(idPlace, token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        apiCallback.callback(MyEventsAdapter.DELETE_PLACE_SUCCESS+idPlace);
                    } else if (apiResult.getCode() == 2) {
                        apiCallback.callback(MyEventsAdapter.DELETE_FAILURE);
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    apiCallback.callback(MyEventsAdapter.DELETE_FAILURE);
                    Log.d("Throw delete place ", throwable.getMessage());
                });
    }

    public void deleteEvent(int idEvent, String token, ApiCallback apiCallback) {
        requests.deleteEvent(idEvent, token).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        apiCallback.callback(MyEventsAdapter.DELETE_EVENT_SUCCESS+idEvent);
                    } else if (apiResult.getCode() == 2) {
                        apiCallback.callback(MyEventsAdapter.DELETE_FAILURE);
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    apiCallback.callback(MyEventsAdapter.DELETE_FAILURE);
                    Log.d("Throw delete event ", throwable.getMessage());
                });
    }

    public void registration(String name, String surname, String email, String password, String city, ApiCallback callback) {
        requests.registration(name, surname, email, password, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
//                        Type type = new TypeToken<UserToken>() {
//                        }.getType();
                        UserToken tokken1 = apiResult.getBody();
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
//                        Type type = new TypeToken<UserToken>() {
//                        }.getType();
                        UserToken tokken1 = apiResult.getBody();
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
//                        Type type = new TypeToken<User>() {
//                        }.getType();
                        User user = apiResult.getBody();
                        callback.callback(user);
                    }
                }, throwable -> {
                    callback.callback(null);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROW OT NURIKA getUser" + throwable.getMessage());
                });
    }

    public void getEvent(int id, ApiCallback callback) {
        requests.getEvent(id, SharedPreferencesProvider.getInstance(context).getUserTokken()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
//                        Type type = new TypeToken<Event>() {
//                        }.getType();
                        Event event = apiResult.getBody();
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
//                        Type type = new TypeToken<Place>() {
//                        }.getType();
                        Place place = apiResult.getBody();
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

    public void sendComplaint(int idPlace, String token, String title, String body, ApiCallback callback) {
        requests.sendComplaint(idPlace, token, title, body).subscribeOn(Schedulers.io())
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
