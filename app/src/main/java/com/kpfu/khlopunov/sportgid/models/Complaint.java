package com.kpfu.khlopunov.sportgid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hlopu on 09.04.2018.
 */

public class Complaint implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("body")
    @Expose
    private int body;
    @SerializedName("token")
    @Expose
    private int userToken;

    public Complaint(int body, int userToken) {
        this.body = body;
        this.userToken = userToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBody() {
        return body;
    }

    public void setBody(int body) {
        this.body = body;
    }

    public int getUserToken() {
        return userToken;
    }

    public void setUserToken(int userToken) {
        this.userToken = userToken;
    }
}
