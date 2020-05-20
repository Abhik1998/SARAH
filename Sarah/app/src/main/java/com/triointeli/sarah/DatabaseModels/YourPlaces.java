package com.triointeli.sarah.DatabaseModels;

import io.realm.RealmObject;

/**
 * Created by Abhik on 5/2/18.
 */

public class YourPlaces extends RealmObject {

    String placeLAT,placeLNG,name;

    public YourPlaces() {
    }

    public YourPlaces(String placeLAT, String placeLNG, String name) {
        this.placeLAT = placeLAT;
        this.placeLNG = placeLNG;
        this.name = name;
    }

    public String getPlaceLAT() {
        return placeLAT;
    }

    public void setPlaceLAT(String placeLAT) {
        this.placeLAT = placeLAT;
    }

    public String getPlaceLNG() {
        return placeLNG;
    }

    public void setPlaceLNG(String placeLNG) {
        this.placeLNG = placeLNG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
