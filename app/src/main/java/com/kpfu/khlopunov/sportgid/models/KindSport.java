package com.kpfu.khlopunov.sportgid.models;

/**
 * Created by hlopu on 12.12.2017.
 */

public class KindSport {
    private String kindSport;
    private String image;

    public KindSport(String kindSport, String image) {
        this.kindSport = kindSport;
        this.image = image;
    }

    public String getKindSport() {
        return kindSport;
    }

    public void setKindSport(String kindSport) {
        this.kindSport = kindSport;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
