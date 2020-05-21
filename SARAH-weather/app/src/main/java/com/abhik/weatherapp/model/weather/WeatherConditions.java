package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for Weather Conditions <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class WeatherConditions implements Parcelable {
    public static final String WEATHER_CONDITIONS_ID_KEY = "id";
    public static final String WEATHER_CONDITIONS_WEATHER_MAIN_KEY = "main";
    public static final String WEATHER_CONDITIONS_DESCRIPTION_KEY = "description";
    public static final String WEATHER_CONDITIONS_ICON_KEY = "icon";

    /** Weather condition id */
    private int mId;

    /** Group of weather parameters (Rain, Snow, Extreme etc.) */
    private String mWeatherMain;

    /** Weather condition within the group */
    private String mWeatherDescription;

    /** Weather icon id */
    private String mWeatherIcon;

    private WeatherConditions(Builder builder) {
        mWeatherMain = builder.mWeatherMain;
        mWeatherDescription = builder.mWeatherDescription;
        mWeatherIcon = builder.mWeatherIcon;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int getId() {
        return mId;
    }

    public String getWeatherMain() {
        return mWeatherMain;
    }

    public String getWeatherDescription() {
        return mWeatherDescription;
    }

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public static final class Builder {
        private String mWeatherMain;
        private String mWeatherDescription;
        private String mWeatherIcon;

        public Builder() {
        }

        public Builder mWeatherMain(String val) {
            mWeatherMain = val;
            return this;
        }

        public Builder mWeatherDescription(String val) {
            mWeatherDescription = val;
            return this;
        }

        public Builder mWeatherIcon(String val) {
            mWeatherIcon = val;
            return this;
        }

        public WeatherConditions build() {
            return new WeatherConditions(this);
        }
    }

    protected WeatherConditions(Parcel in) {
        mId = in.readInt();
        mWeatherMain = in.readString();
        mWeatherDescription = in.readString();
        mWeatherIcon = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mWeatherMain);
        dest.writeString(mWeatherDescription);
        dest.writeString(mWeatherIcon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeatherConditions> CREATOR = new Parcelable.Creator<WeatherConditions>() {
        @Override
        public WeatherConditions createFromParcel(Parcel in) {
            return new WeatherConditions(in);
        }

        @Override
        public WeatherConditions[] newArray(int size) {
            return new WeatherConditions[size];
        }
    };
}
