package com.abhik.weatherapp.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Event for getWeather failure response from the API
 */
public class GetWeatherFailureResponseEvent implements Parcelable {
    String mMessage;

    public String getMessage() {
        return mMessage;
    }

    public GetWeatherFailureResponseEvent(String message){
        this.mMessage = message;
    }

    protected GetWeatherFailureResponseEvent(Parcel in) {
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
    public static final Parcelable.Creator<GetWeatherFailureResponseEvent> CREATOR = new Parcelable.Creator<GetWeatherFailureResponseEvent>() {
        @Override
        public GetWeatherFailureResponseEvent createFromParcel(Parcel in) {
            return new GetWeatherFailureResponseEvent(in);
        }

        @Override
        public GetWeatherFailureResponseEvent[] newArray(int size) {
            return new GetWeatherFailureResponseEvent[size];
        }
    };
}
