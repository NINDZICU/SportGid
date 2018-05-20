package com.kpfu.khlopunov.sportgid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by hlopu on 13.12.2017.
 */

public class Event implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("title")
    @Expose
    private String name;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("place")
    @Expose
    private int place;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("x")
    @Expose
    private int x;
    @SerializedName("y")
    @Expose
    private int y;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("max_of_members")
    @Expose
    private int maxOfMembers;

    @SerializedName("avtor")
    @Expose
    private User author;
    @SerializedName("sport")
    @Expose
    private int sport;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("is_subscribed")
    @Expose
    private boolean is_subscribed;
    @SerializedName("map")
    @Expose
    private int map;
    @SerializedName("members")
    @Expose
    private List<User> members;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews;
//    private KindSport kindSport;


    public Event(int id, String photo, String name, String rating, int place, String address, int x, int y, String price, int maxOfMembers,
                 User author, int sport, String description, boolean is_subscribed, int map, List<User> members, List<Review> reviews) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.rating = rating;
        this.place = place;
        this.address = address;
        this.x = x;
        this.y = y;
        this.price = price;
        this.maxOfMembers = maxOfMembers;
        this.author = author;
        this.sport = sport;
        this.description = description;
        this.is_subscribed = is_subscribed;
        this.map = map;
        this.members = members;
        this.reviews = reviews;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxOfMembers() {
        return maxOfMembers;
    }

    public void setMaxOfMembers(int maxOfMembers) {
        this.maxOfMembers = maxOfMembers;
    }

    public User getAvtor() {
        return author;
    }

    public void setAvtor(User author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public User getAuthor() {
        return author;
    }


    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(boolean is_subscribed) {
        this.is_subscribed = is_subscribed;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}

