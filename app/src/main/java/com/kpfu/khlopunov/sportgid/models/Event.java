package com.kpfu.khlopunov.sportgid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by hlopu on 13.12.2017.
 */

public class Event implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("title")
    @Expose
    private String name;
    @SerializedName("place")
    @Expose
    private Place place;
    @SerializedName("price")
    @Expose
    private String price;
    private String raiting;
    @SerializedName("maxOfMembers")
    @Expose
    private int maxOfMembers;
    @SerializedName("token")
    @Expose
    private String author;
    @SerializedName("description")
    @Expose
    private String description;
    private Set<User> members;
    private KindSport kindSport;

    public Event(int id, String photo, String name, Place place, String price, String raiting, int maxOfMembers, String authorToken, String description, Set<User> members, KindSport kindSport) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.place = place;
        this.price = price;
        this.raiting = raiting;
        this.maxOfMembers = maxOfMembers;
        this.author = authorToken;
        this.description = description;
        this.members = members;
        this.kindSport = kindSport;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
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

    public String getRaiting() {
        return raiting;
    }

    public void setRaiting(String raiting) {
        this.raiting = raiting;
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

    public String getAvtor() {
        return author;
    }

    public void setAvtor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public KindSport getKindSport() {
        return kindSport;
    }

    public void setKindSport(KindSport kindSport) {
        this.kindSport = kindSport;
    }
}

