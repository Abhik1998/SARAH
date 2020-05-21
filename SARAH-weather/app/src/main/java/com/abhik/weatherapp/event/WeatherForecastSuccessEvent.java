package com.abhik.weatherapp.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.abhik.weatherapp.model.weather.WeatherForecastResponse;

/**
 * Event for weather forecast success
 */
public class WeatherForecastSuccessEvent implements Parcelable {
    private final WeatherForecastResponse mWeatherResponse;

    public WeatherForecastSuccessEvent(WeatherForecastResponse weatherResponse) {
        mWeatherResponse = weatherResponse;
    }

    public WeatherForecastResponse getResponse() {
        return mWeatherResponse;
    }

    private WeatherForecastSuccessEvent(Parcel in) {
        mWeatherResponse = (WeatherForecastResponse) in.readValue(WeatherForecastResponse.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mWeatherResponse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeatherForecastSuccessEvent> CREATOR = new Parcelable.Creator<WeatherForecastSuccessEvent>() {
        @Override
        public WeatherForecastSuccessEvent createFromParcel(Parcel in) {
            return new WeatherForecastSuccessEvent(in);
        }

        @Override
        public WeatherForecastSuccessEvent[] newArray(int size) {
            return new WeatherForecastSuccessEvent[size];
        }
    };
}
