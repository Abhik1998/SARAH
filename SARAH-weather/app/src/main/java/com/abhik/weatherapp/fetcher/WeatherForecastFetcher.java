package com.abhik.weatherapp.fetcher;

/**
 * Interface defining methods for weather forecast info.
 * Added/not removed to show how the project can be extended.
 */
public interface WeatherForecastFetcher {
    void getWeatherForecastByCityName(String cityName);
    void getWeatherForecastByGeoLocation(String lat, String lon);

}
