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
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("map")
    @Expose
    private Integer map;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("sport")
    @Expose
    private List<Integer> sport = null;
    @SerializedName("bookingEntrys")
    @Expose
    private List<Integer> bookingEntrys = null;

    public Place(Integer id, String title, Integer rating, String description, String address, Integer price, String photo, String city,
                 List<Review> reviews, Integer map, User user, String contact, List<Integer> sport, List<Integer> bookingEntrys) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.description = description;
        this.address = address;
        this.price = price;
        this.photo = photo;
        this.city = city;
        this.reviews = reviews;
        this.map = map;
        this.user = user;
        this.contact = contact;
        this.sport = sport;
        this.bookingEntrys = bookingEntrys;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<Integer> getSport() {
        return sport;
    }

    public void setSport(List<Integer> sport) {
        this.sport = sport;
    }

    public List<Integer> getBookingEntrys() {
        return bookingEntrys;
    }

    public void setBookingEntrys(List<Integer> bookingEntrys) {
        this.bookingEntrys = bookingEntrys;
    }

    public Integer getMap() {
        return map;
    }

    public void setMap(Integer map) {
        this.map = map;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

