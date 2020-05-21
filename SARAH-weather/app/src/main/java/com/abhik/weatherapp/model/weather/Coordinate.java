package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Coordinate Model <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class Coordinate implements Parcelable {
    public static final String COORDINATE_LATITUDE_KEY = "lat";
    public static final String COORDINATE_LONGITUDE_KEY = "lon";

    /**
     * City geo location
     */
    private double mLat;

    /**
     * City geo  longitude
     */
    private double mLon;

    protected Coordinate(Parcel in) {
        mLat = in.readDouble();
        mLon = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mLat);
        dest.writeDouble(mLon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Coordinate> CREATOR = new Parcelable.Creator<Coordinate>() {
        @Override
        public Coordinate createFromParcel(Parcel in) {
            return new Coordinate(in);
        }

        @Override
        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };
}
