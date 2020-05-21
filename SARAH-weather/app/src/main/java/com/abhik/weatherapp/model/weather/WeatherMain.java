package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

/**
 * Model class for Weather Main <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class WeatherMain implements Parcelable {
    public static final String WEATHER_MAIN_TEMPERATURE_KEY = "temp";
    public static final String WEATHER_MAIN_PRESSURE_KEY = "pressure";
    public static final String WEATHER_MAIN_HUMIDITY_KEY = "humidity";
    public static final String WEATHER_MAIN_TEMPERATURE_MIN_KEY = "temp_min";
    public static final String WEATHER_MAIN_TEMPERATURE_MAX_KEY = "temp_max";

    /** Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit. */
    private double mTemp;

    /** Atmospheric pressure (on the sea level, if there is no sea_level or grnd_level data), hPa */
    private double mPressure;

    /** Humidity, % */
    private double mHumidity;

    /** Minimum temperature at the moment.
     * This is deviation from current temp that is possible for large cities and
     * megalopolises geographically expanded (use these parameter optionally).
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit. */
    private double mMinTemp;

    /** Maximum temperature at the moment.
     * This is deviation from current temp that is possible for large cities and
     * megalopolises geographically expanded (use these parameter optionally).
     * Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit. */
    private double mMaxTemp;

    private WeatherMain(Builder builder) {
        mTemp = builder.mTemp;
        mPressure = builder.mPressure;
        mHumidity = builder.mHumidity;
        mMinTemp = builder.mMinTemp;
        mMaxTemp = builder.mMaxTemp;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public double getPressure() {
        return mPressure;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public double getTemp() {
        return Double.parseDouble(new DecimalFormat("##.##").format(mTemp-273.15));
    }

    public double getMinTemp() {
        return Double.parseDouble(new DecimalFormat("##.##").format(mMinTemp-273.15));
    }

    public double getMaxTemp() {
        return (Double.parseDouble(new DecimalFormat("##.##").format(mMaxTemp-273.15)));
    }

    public static final class Builder {
        private double mTemp;
        private double mPressure;
        private double mHumidity;
        private double mMinTemp;
        private double mMaxTemp;

        public Builder() {
        }

        public Builder mTemp(double val) {
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

        public Builder mMinTemp(double val) {
            mMinTemp = val;
            return this;
        }

        public Builder mMaxTemp(double val) {
            mMaxTemp = val;
            return this;
        }

        public WeatherMain build() {
            return new WeatherMain(this);
        }
    }

    protected WeatherMain(Parcel in) {
        mTemp = in.readDouble();
        mPressure = in.readDouble();
        mHumidity = in.readDouble();
        mMinTemp = in.readDouble();
        mMaxTemp = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mTemp);
        dest.writeDouble(mPressure);
        dest.writeDouble(mHumidity);
        dest.writeDouble(mMinTemp);
        dest.writeDouble(mMaxTemp);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WeatherMain> CREATOR = new Parcelable.Creator<WeatherMain>() {
        @Override
        public WeatherMain createFromParcel(Parcel in) {
            return new WeatherMain(in);
        }

        @Override
        public WeatherMain[] newArray(int size) {
            return new WeatherMain[size];
        }
    };
}
