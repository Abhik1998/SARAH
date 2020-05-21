package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for weather forecast response <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class WeatherForecastResponse implements Parcelable {
    public static final String WEATHER_FORECAST_RESPONSE_CITY_KEY = "city";
    public static final String WEATHER_FORECAST_RESPONSE_CODE_KEY = "cod";
    public static final String WEATHER_FORECAST_RESPONSE_COORDINATES_KEY = "coord";
    public static final String WEATHER_FORECAST_RESPONSE_PARAMS_LIST_KEY = "list";

    /** Weather accuracy message */
    private City mCity;

    /** Weather code */
    private String mCod;

    private Coordinate mCoord;

    /** Weather parameters list */
    private List<WeatherForecastParams> mWeatherList;

    private WeatherForecastResponse(Builder builder) {
        mCity = builder.mCity;
        mCod = builder.mCod;
        mCoord = builder.mCoord;
        mWeatherList = builder.mWeatherList;
    }

    public City getCity() {
        return mCity;
    }
    public String getCod() {
        return mCod;
    }
    public Coordinate getmCoord() {
        return mCoord;
    }
    public List<WeatherForecastParams> getWeatherList() {
        return mWeatherList;
    }

    protected WeatherForecastResponse(Parcel in) {
        mCity = (City) in.readValue(City.class.getClassLoader());
        mCod = in.readString();
        mCoord = (Coordinate) in.readValue(Coordinate.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mWeatherList = new ArrayList<WeatherForecastParams>();
            in.readList(mWeatherList, WeatherForecastParams.class.getClassLoader());
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
        dest.writeValue(mCity);
        dest.writeString(mCod);
        dest.writeValue(mCoord);
        if (mWeatherList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mWeatherList);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<WeatherForecastResponse> CREATOR = new Creator<WeatherForecastResponse>() {
        @Override
        public WeatherForecastResponse createFromParcel(Parcel in) {
            return new WeatherForecastResponse(in);
        }

        @Override
        public WeatherForecastResponse[] newArray(int size) {
            return new WeatherForecastResponse[size];
        }
    };

    public static final class Builder {
        private City mCity;
        private String mCod;
        private Coordinate mCoord;
        private List<WeatherForecastParams> mWeatherList;

        public Builder() {
        }

        public Builder mCity(City val) {
            mCity = val;
            return this;
        }

        public Builder mCod(String val) {
            mCod = val;
            return this;
        }

        public Builder mCoord(Coordinate val) {
            mCoord = val;
            return this;
        }

        public Builder mWeatherList(List<WeatherForecastParams> val) {
            mWeatherList = val;
            return this;
        }

        public WeatherForecastResponse build() {
            return new WeatherForecastResponse(this);
        }
    }
}
