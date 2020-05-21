package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for temperature <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class Temp implements Parcelable {
    public static final String TEMP_DAY_KEY = "day";
    public static final String TEMP_MIN_KEY = "min";
    public static final String TEMP_MAX_KEY = "max";
    public static final String TEMP_NIGHT_KEY = "night";
    public static final String TEMP_EVE_KEY = "eve";
    public static final String TEMP_MORNING_KEY = "morn";

    private double mDay;
    private double mMin;
    private double mMax;
    private double mNight;
    private double mEve;
    private double mMorn;

    private Temp(Builder builder) {
        mDay = builder.mDay;
        mMin = builder.mMin;
        mMax = builder.mMax;
        mNight = builder.mNight;
        mEve = builder.mEve;
        mMorn = builder.mMorn;
    }

    public double getDay() {
        return mDay-273.15;
    }

    public double getMin() {
        return mMin-273.15;
    }

    public double getMax() {
        return mMax-273.15;
    }

    public double getNight() {
        return mNight-273.15;
    }

    public double getEve() {
        return mEve-273.15;
    }

    public double getMorn() {
        return mMorn-273.15;
    }

    protected Temp(Parcel in) {
        mDay = in.readDouble();
        mMin = in.readDouble();
        mMax = in.readDouble();
        mNight = in.readDouble();
        mEve = in.readDouble();
        mMorn = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mDay);
        dest.writeDouble(mMin);
        dest.writeDouble(mMax);
        dest.writeDouble(mNight);
        dest.writeDouble(mEve);
        dest.writeDouble(mMorn);
    }

    @SuppressWarnings("unused")
    public static final Creator<Temp> CREATOR = new Creator<Temp>() {
        @Override
        public Temp createFromParcel(Parcel in) {
            return new Temp(in);
        }

        @Override
        public Temp[] newArray(int size) {
            return new Temp[size];
        }
    };

    public static final class Builder {
        private double mDay;
        private double mMin;
        private double mMax;
        private double mNight;
        private double mEve;
        private double mMorn;

        public Builder() {
        }

        public Builder mDay(double val) {
            mDay = val;
            return this;
        }

        public Builder mMin(double val) {
            mMin = val;
            return this;
        }

        public Builder mMax(double val) {
            mMax = val;
            return this;
        }

        public Builder mNight(double val) {
            mNight = val;
            return this;
        }

        public Builder mEve(double val) {
            mEve = val;
            return this;
        }

        public Builder mMorn(double val) {
            mMorn = val;
            return this;
        }

        public Temp build() {
            return new Temp(this);
        }
    }
}
