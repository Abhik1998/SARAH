package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Weather Forecast Params <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class WeatherForecastParams implements Parcelable {
    public static final String WEATHER_FORECAST_PARAMS_TEMP_KEY = "temp";
    public static final String WEATHER_FORECAST_PARAMS_PRESSURE_KEY = "pressure";
    public static final String WEATHER_FORECAST_PARAMS_HUMIDITY_KEY = "humidity";
    public static final String WEATHER_FORECAST_PARAMS_SPEED_KEY = "speed";
    public static final String WEATHER_FORECAST_PARAMS_DEGREE_KEY = "deg";
    public static final String WEATHER_FORECAST_PARAMS_CLOUDS_KEY = "clouds";
    public static final String WEATHER_FORECAST_PARAMS_DATE_KEY = "dt";
    public static final String WEATHER_FORECAST_PARAMS_WEATHER_KEY = "weather";

    private Temp mTemp;
    private double mPressure;
    private double mHumidity;
    private double mSpeed;
    private double mDeg;
    private int mCloudiness;

    /**
     * Time of data calculation, unix, UTC
     */
    private int mDt;
    private List<WeatherConditions> mWeather;

    private WeatherForecastParams(Builder builder) {
        mTemp = builder.mTemp;
        mPressure = builder.mPressure;
        mHumidity = builder.mHumidity;
        mSpeed = builder.mSpeed;
        mDeg = builder.mDeg;
        mCloudiness = builder.mCloudiness;
        mDt = builder.mDt;
        mWeather = builder.mWeather;
    }

    public List<WeatherConditions> getWeather() {
        return mWeather;
    }
    public Temp getTemp() {
        return mTemp;
    }
    public double getPressure() {
        return mPressure;
    }
    public double getHumidity() {
        return mHumidity;
    }
    public int getDt() {
        return mDt;
    }
    public double getmSpeed() {
        return mSpeed;
    }
    public double getmDeg() {
        return mDeg;
    }
    public int getmCloudiness() {
        return mCloudiness;
    }

    protected WeatherForecastParams(Parcel in) {
        mTemp = (Temp) in.readValue(Temp.class.getClassLoader());
        mPressure = in.readDouble();
        mHumidity = in.readDouble();
        mSpeed = in.readDouble();
        mDeg = in.readDouble();
        mCloudiness = in.readInt();
        mDt = in.readInt();
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
        dest.writeValue(mTemp);
        dest.writeDouble(mPressure);
        dest.writeDouble(mHumidity);
        dest.writeDouble(mSpeed);
        dest.writeDouble(mDeg);
        dest.writeInt(mCloudiness);
        dest.writeInt(mDt);
        if (mWeather == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mWeather);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<WeatherForecastParams> CREATOR = new Creator<WeatherForecastParams>() {
        @Override
        public WeatherForecastParams createFromParcel(Parcel in) {
            return new WeatherForecastParams(in);
        }

        @Override
        public WeatherForecastParams[] newArray(int size) {
            return new WeatherForecastParams[size];
        }
    };

    public static final class Builder {
        private Temp mTemp;
        private double mPressure;
        private double mHumidity;
        private double mSpeed;
        private double mDeg;
        private int mCloudiness;
        private int mDt;
        private List<WeatherConditions> mWeather;

        public Builder() {
        }

        public Builder mTemp(Temp val) {
            mTemp = val;
            return this;
        }

        public Builder mPressure(double val) {
            mPressure = val;
            return this;
        }

        public Builder mHumidity(double val) {
            mHumidity = val;
            return this;
        }

        public Builder mSpeed(double val) {
            mSpeed = val;
            return this;
        }

        public Builder mDeg(double val) {
            mDeg = val;
            return this;
        }

        public Builder mCloudiness(int val) {
            mCloudiness = val;
            return this;
        }

        public Builder mDt(int val) {
            mDt = val;
            return this;
        }

        public Builder mWeather(List<WeatherConditions> val) {
            mWeather = val;
            return this;
        }

        public WeatherForecastParams build() {
            return new WeatherForecastParams(this);
        }
    }
}
