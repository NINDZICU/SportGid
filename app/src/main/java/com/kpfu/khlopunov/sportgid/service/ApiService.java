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
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.User;

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

    public void registrationUser(String name, String surname, String login, String password) {
        sportGidApi.registration(new User(name, surname, login, password)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(code -> {
                }, throwable ->
                        Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public List<KindSport> getKindSports() {
        List<KindSport> kinds = new ArrayList<>();
        requests.getKindSports().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<List<KindSport>>() {
                        }.getType();
                        List<KindSport> kindSports = gson.fromJson(apiResult.getBody().toString(), type);
                        kinds.addAll(kindSports);
                    }
                }, throwable -> {
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROOOOOWOWOWOWW OT NURIKA " + throwable.getMessage());
                });
        return kinds;
    }

    public List<Event> getEvents(int id, String city, EventAdapter adapter) {
        List<Event> events = new ArrayList<>();
        requests.getEvents(id, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if(apiResult.getCode()==0){
                        Type type = new TypeToken<List<Event>>() {}.getType();
                        List<Event> events1 = gson.fromJson(apiResult.getBody().toString(), type);
                        events.addAll(events1);
                        adapter.setmEventList(events1);
                        adapter.notifyData();
                    }
                }, throwable -> {
                    adapter.notifyData();
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROOOOOWOWOWOWW OT NURIKA " + throwable.getMessage());
                });
        return events;
    }

    public List<Place> getPlaces(int id, String city, PlaceAdapter adapter) {
        List<Place> places = new ArrayList<>();
        requests.getPlaces(id, city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    if (apiResult.getCode() == 0) {
                        Type type = new TypeToken<List<Place>>() {}.getType();
                        List<Place> places1 = gson.fromJson(apiResult.getBody().toString(), type);
                        places.addAll(places1);
                        adapter.setmPlaceList(places1);
                        adapter.notifyData();
                    }
                }, throwable -> {
                    adapter.notifyData();
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROOOOOWOWOWOWW OT NURIKA " + throwable.getMessage());
                });
        return places;
    }

    public boolean addPlace(String address, String contact, String title, String description, String city, List<Long> kindOfSport) {
        List<Boolean> flags = new ArrayList<>();
        System.out.println("CODE NURIK BESIT1111111 ");
        requests.addPlace(address, contact, title, description, city, kindOfSport).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResult -> {
                    System.out.println("CODE NURIK BESIT " + apiResult.getCode());
                    if (apiResult.getCode() == 0) {
                        flags.add(true);
                        Toast.makeText(context, "Добавление успешно выполнено!!", Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    flags.add(false);
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROOOOOWOWOWOWW OT NURIKA " + throwable.getMessage());
                });
        return flags.get(0);
    }
}
