package com.kpfu.khlopunov.sportgid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by hlopu on 16.12.2017.
 */

public class Place implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    private User user;
    private KindSport sport;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews;
    private Set<Event> events;
    //    private Set<Complaint> complaints;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("photo")
    @Expose
    private String photo;

    public Place(int id, String title, int rating, String description, String address, String city,
                 User user, KindSport sport, List<Review> reviews, Set<Event> events, String contact, String photo) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.description = description;
        this.address = address;
        this.city = city;
        this.user = user;
        this.sport = sport;
        this.reviews = reviews;
        this.events = events;
        this.contact = contact;
        this.photo = photo;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

