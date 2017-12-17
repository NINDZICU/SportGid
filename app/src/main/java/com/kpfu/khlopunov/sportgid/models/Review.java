/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kpfu.khlopunov.sportgid.models;

import java.io.Serializable;

public class Review implements Serializable{

    private int id;
    private Long date;
    private User user;
    private String body;
    private Place place;
    private Event event;
    private int rating;

    public Review(int id, Long date, User user, String body, Place place, Event event, int rating) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.body = body;
        this.place = place;
        this.event = event;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
