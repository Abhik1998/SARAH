package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for Wind <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class Wind implements Parcelable {
    public static final String WIND_SPEED_KEY = "speed";
    public static final String WIND_DEGREE_KEY = "deg";

    /** Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour. */
    private double mSpeed;

    /** Wind direction, degrees (meteorological) */
    private double mDegree;

    public double getmSpeed() {
        return mSpeed;
    }

    public double getmDegree() {
        return mDegree;
    }

    protected Wind(Parcel in) {
        mSpeed = in.readDouble();
        mDegree = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mSpeed);
        dest.writeDouble(mDegree);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Wind> CREATOR = new Parcelable.Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };
}
