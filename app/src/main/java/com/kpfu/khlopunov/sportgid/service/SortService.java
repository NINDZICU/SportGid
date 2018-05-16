package com.kpfu.khlopunov.sportgid.service;

import com.kpfu.khlopunov.sportgid.models.Event;
import com.kpfu.khlopunov.sportgid.models.KindSport;
import com.kpfu.khlopunov.sportgid.models.Place;

import java.util.Collections;
import java.util.List;

/**
 * Created by hlopu on 18.12.2017.
 */

public class SortService {
    public static int ASC = 0;
    public static int DESC = 1;

    public List<Event> sortEvents(List<Event> events, String parameter, int asc) {
        switch (parameter) {
            case "name":
                Collections.sort(events, (event1, event2) -> event1.getName().compareTo(event2.getName()));
                return events;
            case "price":
                Collections.sort(events, (event1, event2) -> Integer.valueOf(event1.getPrice()) - Integer.valueOf(event2.getPrice()));
                return events;

            case "rating":
                //TODO сортировка не только по целым числам а и по флоат
                Collections.sort(events, (event1, event2) -> Integer.valueOf(event1.getRating()) - Integer.valueOf(event2.getRating()));
                return events;

        }
        return events;
    }

    public List<Place> sortPlaces(List<Place> places, String parameter, int asc) {
        switch (parameter) {
            case "name":
                Collections.sort(places, (event1, event2) -> event1.getTitle().compareTo(event2.getTitle()));
                return places;
//            case "price":
//                Collections.sort(places, (event1, event2) -> Integer.valueOf(event1.getPrice()) - Integer.valueOf(event2.getPrice()));
//                return places;
            case "rating":
                Collections.sort(places, (event1, event2) -> event1.getRating() - event2.getRating());
                return places;
        }
        return places;
    }
}
