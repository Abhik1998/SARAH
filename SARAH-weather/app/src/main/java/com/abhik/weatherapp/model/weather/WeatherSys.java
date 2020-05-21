package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for Weather Sys <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class WeatherSys implements Parcelable {
    public static final String WEATHER_SYS_COUNTRY_KEY = "country";
    public static final String WEATHER_SYS_SUNRISE_KEY = "sunrise";
    public static final String WEATHER_SYS_SUNSET_KEY = "sunset";

    /** Country code (GB, JP etc.) */
    private String mCountry;

    /** Sunrise time, unix, UTC */
    private long mSunrise = 0L;

    /** Sunset time, unix, UTC */
    private long mSunset = 0L;

    private WeatherSys(Builder builder) {
        mCountry = builder.mCountry;
        mSunrise = builder.mSunrise;
        mSunset = builder.mSunset;
    }

    public String getCountry() {
        return mCountry;
    }

    public long getSunrise() {
        return mSunrise;
    }

    public long getSunset() {
        return mSunset;
    }

    protected WeatherSys(Parcel in) {
        mCountry = in.readString();
        mSunrise = in.readLong();
        mSunset = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCountry);
        dest.writeLong(mSunrise);
        dest.writeLong(mSunset);
    }

    @SuppressWarnings("unused")
    public static final Creator<WeatherSys> CREATOR = new Creator<WeatherSys>() {
        @Override
        public WeatherSys createFromParcel(Parcel in) {
            return new WeatherSys(in);
        }

        @Override
        public WeatherSys[] newArray(int size) {
            return new WeatherSys[size];
        }
    };

    public static final class Builder {
        private String mCountry;
        private long mSunrise;
        private long mSunset;

        public Builder() {
        }

        public Builder mCountry(String val) {
            mCountry = val;
            return this;
        }

        public Builder mSunrise(long val) {
            mSunrise = val;
            return this;
        }

        public Builder mSunset(long val) {
            mSunset = val;
            return this;
        }

        public WeatherSys build() {
            return new WeatherSys(this);
        }
    }
}
