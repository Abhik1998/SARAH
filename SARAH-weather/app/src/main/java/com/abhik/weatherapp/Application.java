package com.abhik.weatherapp;

import com.abhik.weatherapp.module.ApplicationModule;

/**
 * Class extends application to do application level stuff.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationModule.setApplication(this);
    }
}
