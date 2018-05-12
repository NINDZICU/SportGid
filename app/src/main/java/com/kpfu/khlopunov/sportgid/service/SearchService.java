package com.kpfu.khlopunov.sportgid.service;

import android.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by hlopu on 12.05.2018.
 */

public class SearchService {
    public static Observable<String> fromView(SearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                subject.onNext(text);
                return true;
            }
        });


        return subject;
    }
}
