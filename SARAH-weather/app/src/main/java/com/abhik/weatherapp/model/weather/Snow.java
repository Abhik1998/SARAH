package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class for Snow <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class Snow implements Parcelable {
    public static final String SNOW_THREE_HOURS_KEY = "3h";

    /** Snow volume for the last 3 hours */
    private int mLast3Hrs;

    protected Snow(Parcel in) {
        mLast3Hrs = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLast3Hrs);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Snow> CREATOR = new Parcelable.Creator<Snow>() {
        @Override
        public Snow createFromParcel(Parcel in) {
            return new Snow(in);
        }

        @Override
        public Snow[] newArray(int size) {
            return new Snow[size];
        }
    };
}
