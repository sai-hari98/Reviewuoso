package com.example.sai_h.reviewuoso;

/**
 * Created by sai_h on 03-03-2018.
 */

public class Placeresult {
    String rating;
    String name;
    String vicinity;
    public Placeresult(String rating, String name, String vicinity) {
        this.rating = rating;
        this.name = name;
        this.vicinity = vicinity;
    }

    public String getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }
}
