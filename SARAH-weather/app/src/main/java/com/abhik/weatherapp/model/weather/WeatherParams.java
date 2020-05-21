package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Weather parameters <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class WeatherParams implements Parcelable {
    public static final String WEATHER_PARAMS_ID_KEY = "id";
    public static final String WEATHER_PARAMS_NAME_KEY = "name";
    public static final String WEATHER_PARAMS_COORDINATE_KEY = "coord";
    public static final String WEATHER_PARAMS_MAIN_KEY = "main";
    public static final String WEATHER_PARAMS_DATE_KEY = "dt";
    public static final String WEATHER_PARAMS_WIND_KEY = "wind";
    public static final String WEATHER_PARAMS_SYS_KEY = "sys";
    public static final String WEATHER_PARAMS_RAIN_KEY = "rain";
    public static final String WEATHER_PARAMS_SNOW_KEY = "snow";
    public static final String WEATHER_PARAMS_CLOUDS_KEY = "clouds";
    public static final String WEATHER_PARAMS_WEATHER_KEY = "weather";

    /**
     * City ID
     */
    private int mId;

    /**
     * City name
     */
    private String mName;

    /**
     * City coordinates
     */
    private Coordinate mCoord;

    /**
     * City weather main
     */
    private WeatherMain mMain;

    /**
     * Time of data calculation, unix, UTC
     */
    private int mDt;

    /**
     * City weather wind
     */
    private Wind mWind;

    /**
     * City (country, sunrise, sunset)
     */
    private WeatherSys mSys;

    /**
     * City rain info
     */
    private Rain mRain;

    /**
     * City snow info
     */
    private Snow mSnow;

    /**
     * City cloudiness info
     */
    private Clouds mClouds;

    /**
     * City weather more info
     */
    private List<WeatherConditions> mWeather;

    private WeatherParams(Builder builder) {
        mId = builder.mId;
        mName = builder.mName;
        mCoord = builder.mCoord;
        mMain = builder.mMain;
        mDt = builder.mDt;
        mWind = builder.mWind;
        mSys = builder.mSys;
        mRain = builder.mRain;
        mSnow = builder.mSnow;
        mClouds = builder.mClouds;
        mWeather = builder.mWeather;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Coordinate getCoord() {
        return mCoord;
    }

    public WeatherMain getMain() {
        return mMain;
    }

    public int getDt() {
        return mDt;
    }

    public Wind getWind() {
        return mWind;
    }

    public WeatherSys getSys() {
        return mSys;
    }

    public Rain getRain() {
        return mRain;
    }

    public Snow getSnow() {
        return mSnow;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public List<WeatherConditions> getWeather() {
        return mWeather;
    }

    protected WeatherParams(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mCoord = (Coordinate) in.readValue(Coordinate.class.getClassLoader());
        mMain = (WeatherMain) in.readValue(WeatherMain.class.getClassLoader());
        mDt = in.readInt();
        mWind = (Wind) in.readValue(Wind.class.getClassLoader());
        mSys = (WeatherSys) in.readValue(WeatherSys.class.getClassLoader());
        mRain = (Rain) in.readValue(Rain.class.getClassLoader());
        mSnow = (Snow) in.readValue(Snow.class.getClassLoader());
        mClouds = (Clouds) in.readValue(Clouds.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mWeather = new ArrayList<WeatherConditions>();
            in.readList(mWeather, WeatherConditions.class.getClassLoader());
        } else {
            mWeather = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeValue(mCoord);
        dest.writeValue(mMain);
        dest.writeInt(mDt);
        dest.writeValue(mWind);
        dest.writeValue(mSys);
        dest.writeValue(mRain);
        dest.writeValue(mSnow);
        dest.writeValue(mClouds);
        if (mWeather == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mWeather);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<WeatherParams> CREATOR = new Creator<WeatherParams>() {
        @Override
        public WeatherParams createFromParcel(Parcel in) {
            return new WeatherParams(in);
        }

        @Override
        public WeatherParams[] newArray(int size) {
            return new WeatherParams[size];
        }
    };

    public static final class Builder {
        private int mId;
        private String mName;
        private Coordinate mCoord;
        private WeatherMain mMain;
        private int mDt;
        private Wind mWind;
        private WeatherSys mSys;
        private Rain mRain;
        private Snow mSnow;
        private Clouds mClouds;
        private List<WeatherConditions> mWeather;

        public Builder() {
        }

        public Builder mId(int val) {
            mId = val;
            return this;
        }

        public Builder mName(String val) {
            mName = val;
            return this;
        }

        public Builder mCoord(Coordinate val) {
            mCoord = val;
            return this;
        }

        public Builder mMain(WeatherMain val) {
            mMain = val;
            return this;
        }

        public Builder mDt(int val) {
            mDt = val;
            return this;
        }

        public Builder mWind(Wind val) {
            mWind = val;
            return this;
        }

        public Builder mSys(WeatherSys val) {
            mSys = val;
            return this;
        }

        public Builder mRain(Rain val) {
            mRain = val;
            return this;
        }

        public Builder mSnow(Snow val) {
            mSnow = val;
            return this;
        }

        public Builder mClouds(Clouds val) {
            mClouds = val;
            return this;
        }

        public Builder mWeather(List<WeatherConditions> val) {
            mWeather = val;
            return this;
        }

        public WeatherParams build() {
            return new WeatherParams(this);
        }
    }
}
