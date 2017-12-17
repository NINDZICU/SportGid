package com.kpfu.khlopunov.sportgid.models;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by hlopu on 16.12.2017.
 */

public class Place implements Serializable {

    private int id;
    private String title;
    private int rating;
    private String description;
    private String address;
    private String city;
    private User user;
    private KindSport sport;
//    private Set<Review> reviews;
    private Set<Event> events;
//    private Set<Complaint> complaints;
    private String contact;

    public Place(int id, String title, int rating, String description, String address, String city, User user, KindSport sport, Set<Event> events, String contact) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.description = description;
        this.address = address;
        this.city = city;
        this.user = user;
        this.sport = sport;
        this.events = events;
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public KindSport getSport() {
        return sport;
    }

    public void setSport(KindSport sport) {
        this.sport = sport;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

