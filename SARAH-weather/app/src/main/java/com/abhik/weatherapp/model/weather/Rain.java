package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for Rain <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class Rain implements Parcelable {
    public static final String RAIN_THREE_HOURS_KEY = "3h";

    /** Rain volume for the last 3 hours */
    private float mLast3Hrs;

    protected Rain(Parcel in) {
        mLast3Hrs = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mLast3Hrs);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Rain> CREATOR = new Parcelable.Creator<Rain>() {
        @Override
        public Rain createFromParcel(Parcel in) {
            return new Rain(in);
        }

        @Override
        public Rain[] newArray(int size) {
            return new Rain[size];
        }
    };
}