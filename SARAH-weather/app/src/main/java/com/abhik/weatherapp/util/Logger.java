package com.abhik.weatherapp.util;

import android.text.TextUtils;
import android.util.Log;

import com.abhik.weatherapp.BuildConfig;

/**
 * Wrapper for android {@link Log Log} class to prevent Logs from printing to console in releases.
 */
public class Logger {
    private static boolean sIsDebug = BuildConfig.DEBUG;

    //private constructor
    private Logger() {
    }

    public static void i(String tag, String message) {
        if (sIsDebug) {
            if(!TextUtils.isEmpty(message)){
                Log.i(tag, message);
            }
        }
    }

    public static void d(String tag, String message) {
        if (sIsDebug) {
            if(!TextUtils.isEmpty(message)){
                Log.d(tag, message);
            }
        }
    }

    public static void w(String tag, String message) {
        if (sIsDebug) {
            if(!TextUtils.isEmpty(message)){
                Log.w(tag, message);
            }
        }
    }

    public static void w(String tag, String message, Throwable t) {
        if (sIsDebug) {
            if(!TextUtils.isEmpty(message)){
                Log.w(tag, message, t);
            }
        }
    }

    public static void e(String tag, String message) {
        if (sIsDebug) {
            if(!TextUtils.isEmpty(message)){
                Log.e(tag, message);
            }
        }
    }

    public static void e(String tag, String message, Throwable t) {
        if (sIsDebug) {
            if(!TextUtils.isEmpty(message)){
                Log.e(tag, message, t);
            }
        }
    }
}