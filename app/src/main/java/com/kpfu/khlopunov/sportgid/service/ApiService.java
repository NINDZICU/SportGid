package com.kpfu.khlopunov.sportgid.service;

import android.content.Context;
import android.widget.Toast;

import com.kpfu.khlopunov.sportgid.api.SportGidApi;
import com.kpfu.khlopunov.sportgid.models.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hlopu on 07.11.2017.
 */

public class ApiService {

    private Context context;
    private SportGidApi sportGidApi;

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
}
