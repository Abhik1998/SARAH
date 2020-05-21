package com.abhik.weatherapp.module;

import com.abhik.weatherapp.network.HttpUrlConnectionNetworkManager;
import com.abhik.weatherapp.network.NetworkManager;
import com.abhik.weatherapp.network.OpenWeatherMapJsonResponseParser;
import com.abhik.weatherapp.network.OpenWeatherMapResponseParser;

import static com.abhik.weatherapp.module.ApplicationModule.resources;

/**
 * Network Manager Module to facilitate injecting network manager and JSONParser.
 */
class NetworkManagerModule {
    static NetworkManager httpUrlConnectionNetworkManager(){
        return new HttpUrlConnectionNetworkManager(resources());
    }

    static OpenWeatherMapResponseParser jsonResponseParser(){
        return new OpenWeatherMapJsonResponseParser();
    }
}
