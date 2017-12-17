package com.kpfu.khlopunov.sportgid.service;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.adapters.EventAdapter;
import com.kpfu.khlopunov.sportgid.adapters.KindSportsAdapter;
import com.kpfu.khlopunov.sportgid.api.SportGidApi;
import com.kpfu.khlopunov.sportgid.api.SportGidApiRequests;
import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;
import com.kpfu.khlopunov.sportgid.models.User;

import java.util.ArrayList;
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

    public ApiService(Context context){
        this.context = context;
    }

    public void registrationUser(String name, String surname, String login, String password) {
        sportGidApi.registration(new User(name, surname, login, password)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(code -> {
                }, throwable ->
                        Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public List<KindSport> getKindSports(KindSportsAdapter adapter){
         List<KindSport> kinds = new ArrayList<>();
        requests.getKindSports().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(kindSports -> {
                    adapter.setKindSports(kindSports);
                }, throwable -> {
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROOOOOWOWOWOWW OT NURIKA "+throwable.getMessage());
                });
        return kinds;
    }

    public List<Event> getEvents(int id, String city, EventAdapter adapter){
        List<Event> events =new ArrayList<>();
        requests.getEvents(id,city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(events1 -> {
                    adapter.setmEventList(events1);
                },throwable -> {
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROOOOOWOWOWOWW OT NURIKA "+throwable.getMessage());
                });
        return events;
    }
    public List<Place> getPlaces(int id, String city, PlaceAdapter adapter){
        List<Place> places =new ArrayList<>();
        requests.getPlaces(id,city).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(places1 -> {
                    adapter.setmEventList(places1);
                },throwable -> {
                    Toast.makeText(context, "Throw " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("THROOOOOWOWOWOWW OT NURIKA "+throwable.getMessage());
                });
        return places;
    }
}
