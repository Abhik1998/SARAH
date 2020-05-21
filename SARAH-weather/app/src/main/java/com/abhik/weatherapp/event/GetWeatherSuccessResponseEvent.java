package com.abhik.weatherapp.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.abhik.weatherapp.model.weather.WeatherResponse;

/**
 * Event for getWeather Success response from the API
 */
public class GetWeatherSuccessResponseEvent implements Parcelable {
    private final WeatherResponse mWeatherResponse;

    public GetWeatherSuccessResponseEvent(WeatherResponse weatherResponse) {
        mWeatherResponse = weatherResponse;
    }

    public WeatherResponse getResponse() {
        return mWeatherResponse;
    }

    private GetWeatherSuccessResponseEvent(Parcel in) {
        mWeatherResponse = (WeatherResponse) in.readValue(WeatherResponse.class.getClassLoader());
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
    public static final Parcelable.Creator<GetWeatherSuccessResponseEvent> CREATOR = new Parcelable.Creator<GetWeatherSuccessResponseEvent>() {
        @Override
        public GetWeatherSuccessResponseEvent createFromParcel(Parcel in) {
            return new GetWeatherSuccessResponseEvent(in);
        }

        @Override
        public GetWeatherSuccessResponseEvent[] newArray(int size) {
            return new GetWeatherSuccessResponseEvent[size];
        }
    };
}
