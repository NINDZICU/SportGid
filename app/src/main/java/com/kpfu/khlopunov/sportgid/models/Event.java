package com.kpfu.khlopunov.sportgid.models;

/**
 * Created by hlopu on 13.12.2017.
 */

public class Event {
    private String image;
    private String name;
    private String address;
    private String price;
    private String raiting;

    public Event(String image, String name, String address, String price, String raiting) {
        this.image = image;
        this.name = name;
        this.address = address;
        this.price = price;
        this.raiting = raiting;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
