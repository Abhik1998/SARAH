package com.abhik.weatherapp.module;


import com.abhik.weatherapp.fetcher.WeatherFetcher;
import com.abhik.weatherapp.fetcher.WeatherForecastFetcher;
import com.abhik.weatherapp.fetcher.WeatherForecastNetworkFetcher;
import com.abhik.weatherapp.fetcher.WeatherNetworkFetcher;

/**
 * Module for fetcher/loader to facilitate dependency injection
 */
public class FetcherModule {
    public static WeatherFetcher WeatherFetcher() {
        return new WeatherNetworkFetcher(NetworkManagerModule.httpUrlConnectionNetworkManager(),
                NetworkManagerModule.jsonResponseParser(), ApplicationModule.resources(), ApplicationModule.getBroadcastManager());
    }

    public static WeatherForecastFetcher WeatherForecastFetcher() {
        return new WeatherForecastNetworkFetcher(NetworkManagerModule.httpUrlConnectionNetworkManager(),
                NetworkManagerModule.jsonResponseParser(), ApplicationModule.resources());
    }
}
