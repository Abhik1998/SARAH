package com.abhik.weatherapp.util;

import android.util.Log;

import com.abhik.weatherapp.module.ApplicationModule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper file for background and data format converter.
 */
public class BackgroundHelper {
    private static final String TAG = BackgroundHelper.class.getSimpleName();

    /**
     * Returns background to be applied to the particular screen based on time of day
     * Just an addon to task!
     *
     * @return resource id for the drawable
     */
    public static int getBackground(){
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        int Hr24=c.get(Calendar.HOUR_OF_DAY);
        Log.i(TAG, "Hour of day: "+Hr24);
        if(Hr24>=19 || Hr24<5){
            return R.drawable.night;
        }else if(Hr24>=5 && Hr24<12) {
            return R.drawable.sunrise;
        }else if(Hr24>=12&&Hr24<16){
            return R.drawable.afternoon;
        }else{
            return R.drawable.sunset;
        }
    }

    /**
     * Helper function to format Long object to specified dateformat
     * @param unixSeconds
     * @return String in specified format
     */
    public static String getDateAsString(Long unixSeconds) {
        // *1000 is to convert seconds to milliseconds
        Date date = new Date(unixSeconds * 1000L);

        // the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat(ApplicationModule.applicationContext()
                .getString(R.string.app_date_format), Locale.US);

        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }
}
