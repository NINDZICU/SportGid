package com.kpfu.khlopunov.sportgid.models;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by hlopu on 13.12.2017.
 */

public class Event implements Serializable{

    private int id;
    private String image;
    private String name;
    private Place place;
    private String price;
    private String raiting;
    private int maxOfMembers;
    private User avtor;
    private String description;
    private Set<User> members;
    private KindSport kindSport;

    public Event(int id, String image, String name, Place place, String price, String raiting, int maxOfMembers, User avtor, String description, Set<User> members, KindSport kindSport) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.place = place;
        this.price = price;
        this.raiting = raiting;
        this.maxOfMembers = maxOfMembers;
        this.avtor = avtor;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public User getAvtor() {
        return avtor;
    }

    public void setAvtor(User avtor) {
        this.avtor = avtor;
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

