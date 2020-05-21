package com.abhik.weatherapp.fetcher;

/**
 * Interface defining methods for fetching weather info
 */
public interface WeatherFetcher {
    void getWeatherByGeoData(String lat, String lon);
    void getWeatherByCityName(String cityName);
}
