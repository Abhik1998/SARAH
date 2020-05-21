package com.abhik.weatherapp.module;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * Application module facilitates access to app wide services and object by any class.
 */
public class ApplicationModule {
    private static Application sApplication;

    public static void setApplication(Application application) {
        sApplication = application;
    }

    public static Context applicationContext() {
        return sApplication;
    }

    public static Resources resources() {
        return sApplication.getResources();
    }

    public static FusedLocationProviderClient getFusedLocationClient (){
        return LocationServices.getFusedLocationProviderClient(applicationContext());
    }

    public static LocalBroadcastManager getBroadcastManager(){
        return LocalBroadcastManager.getInstance(applicationContext());
    }
}
