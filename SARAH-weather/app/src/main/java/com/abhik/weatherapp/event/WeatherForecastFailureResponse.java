package com.abhik.weatherapp.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Event for Weather Forecast failure
 */
public class WeatherForecastFailureResponse implements Parcelable {
    String mMessage;

    public String getMessage() {
        return mMessage;
    }

    public WeatherForecastFailureResponse(String message){
        this.mMessage = message;
    }

    protected WeatherForecastFailureResponse(Parcel in) {
        mMessage = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessage);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeatherForecastFailureResponse> CREATOR = new Parcelable.Creator<WeatherForecastFailureResponse>() {
        @Override
        public WeatherForecastFailureResponse createFromParcel(Parcel in) {
            return new WeatherForecastFailureResponse(in);
        }

        @Override
        public WeatherForecastFailureResponse[] newArray(int size) {
            return new WeatherForecastFailureResponse[size];
        }
    };
}
