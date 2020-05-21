package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * City Model <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class City implements Parcelable {
    public static final String CITY_NAME_KEY = "name";

    private City(Builder builder) {
        mCityName = builder.mCityName;
    }

    public String getmCityName() {
        return mCityName;
    }

    private String mCityName;

    protected City(Parcel in) {
        mCityName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCityName);
    }

    @SuppressWarnings("unused")
    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    public static final class Builder {
        private String mCityName;

        public Builder() {
        }

        public Builder mCityName(String val) {
            mCityName = val;
            return this;
        }

        public City build() {
            return new City(this);
        }
    }
}
