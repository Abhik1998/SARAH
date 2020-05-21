package com.abhik.weatherapp.model.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Cloud Model <br>
 *
 * Check <a href="https://openweathermap.org/api">documentation</a>
 */
public class Clouds implements Parcelable {
    public static final String CLOUDS_CLOUDINESS_KEY = "all";

    /** Cloudiness, % */
    private int mCloudiness;

    public int getCloudiness() {
        return mCloudiness;
    }

    protected Clouds(Parcel in) {
        mCloudiness = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCloudiness);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Clouds> CREATOR = new Parcelable.Creator<Clouds>() {
        @Override
        public Clouds createFromParcel(Parcel in) {
            return new Clouds(in);
        }

        @Override
        public Clouds[] newArray(int size) {
            return new Clouds[size];
        }
    };
}
