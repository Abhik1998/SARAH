package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for weather response <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class WeatherResponse implements Parcelable {
    public static final String WEATHER_RESPONSE_MESSAGE_KEY = "message";
    public static final String WEATHER_RESPONSE_CODE_KEY = "cod";
    public static final String WEATHER_RESPONSE_COUNT_KEY = "count";
    public static final String WEATHER_RESPONSE_WEATHER_PARAMETERS_LIST_KEY = "list";

    /** Weather accuracy message */
    private String mAccuracy;

    /** Weather code */
    private String mCod;

    /** City count. provided city name as input */
    private int mCityCount;

    /** Weather parameters list */
    private List<WeatherParams> mWeatherList;

    private WeatherResponse(Builder builder) {
        mAccuracy = builder.mAccuracy;
        mCod = builder.mCod;
        mWeatherList = builder.mWeatherList;
    }

    public String getAccuracy() {
        return mAccuracy;
    }

    public String getCod() {
        return mCod;
    }

    public int getCityCount() {
        return mCityCount;
    }

    public List<WeatherParams> getWeatherList() {
        return mWeatherList;
    }

    public static final class Builder {
        private String mAccuracy;
        private String mCod;
        private List<WeatherParams> mWeatherList;

        public Builder() {
        }

        public Builder mAccuracy(String val) {
            mAccuracy = val;
            return this;
        }

        public Builder mCod(String val) {
            mCod = val;
            return this;
        }

        public Builder mWeatherList(List<WeatherParams> val) {
            mWeatherList = val;
            return this;
        }

        public WeatherResponse build() {
            return new WeatherResponse(this);
        }
    }

    protected WeatherResponse(Parcel in) {
        mAccuracy = in.readString();
        mCod = in.readString();
        mCityCount = in.readInt();
        if (in.readByte() == 0x01) {
            mWeatherList = new ArrayList<WeatherParams>();
            in.readList(mWeatherList, WeatherParams.class.getClassLoader());
        } else {
            mWeatherList = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAccuracy);
        dest.writeString(mCod);
        dest.writeInt(mCityCount);
        if (mWeatherList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mWeatherList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeatherResponse> CREATOR = new Parcelable.Creator<WeatherResponse>() {
        @Override
        public WeatherResponse createFromParcel(Parcel in) {
            return new WeatherResponse(in);
        }

        @Override
        public WeatherResponse[] newArray(int size) {
            return new WeatherResponse[size];
        }
    };
}
