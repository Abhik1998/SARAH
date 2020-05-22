package com.abhik.weatherapp.fetcher;

import android.content.Intent;
import android.content.res.Resources;

import com.abhik.weatherapp.model.weather.WeatherForecastResponse;
import com.abhik.weatherapp.module.ApplicationModule;
import com.abhik.weatherapp.R;
import com.abhik.weatherapp.event.GetWeatherFailureResponseEvent;
import com.abhik.weatherapp.event.WeatherForecastSuccessEvent;
import com.abhik.weatherapp.network.NetworkManager;
import com.abhik.weatherapp.network.OpenWeatherMapResponseParser;
import com.abhik.weatherapp.util.Logger;

import org.json.JSONException;

import java.net.HttpURLConnection;

/**
 * Implementation class for getting weather forecast.
 *
 * <p>Not used but coded to show extensibility of the architecture...</p>
 *
 */
public class WeatherForecastNetworkFetcher implements WeatherForecastFetcher{
    private static final String TAG = WeatherForecastNetworkFetcher.class.getSimpleName();
    private final NetworkManager mNetworkManager;
    private final OpenWeatherMapResponseParser mResponseParser;
    private final Resources mResources;

    public WeatherForecastNetworkFetcher(NetworkManager networkManager,
                                         OpenWeatherMapResponseParser responseParser,
                                         Resources resources){
        this.mNetworkManager = networkManager;
        this.mResponseParser = responseParser;
        this.mResources = resources;
    }

    @Override
    public void getWeatherForecastByCityName(String cityName) {
        String OPEN_WEATHER_MAP_GET_WEATHER_FORECAST_BY_CITY_NAME_ENDPOINT =
                "/data/2.5/forecast/daily?APPID=%s&q=%s&cnt=1";
        String apiEndpoint = String.format(OPEN_WEATHER_MAP_GET_WEATHER_FORECAST_BY_CITY_NAME_ENDPOINT,
                mResources.getString(R.string.weather_open_weather_map_appid),
                cityName);

        // Call to network manager to fetch weather forecast
        requestOneDayWeatherForecastAndHandleResponse(apiEndpoint);
    }

    @Override
    public void getWeatherForecastByGeoLocation(String lat, String lon) {
        String OPEN_WEATHER_MAP_GET_WEATHER_BY_GEO_DATA_ENDPOINT =
                "/data/2.5/find?lat=%s&lon=%s&cnt=1&APPID=%s";
        String apiEndpoint = String.format(OPEN_WEATHER_MAP_GET_WEATHER_BY_GEO_DATA_ENDPOINT,
                lat, lon, mResources.getString(R.string.weather_open_weather_map_appid));

        // Call to network manager to fetch weather forecast
        requestOneDayWeatherForecastAndHandleResponse(apiEndpoint);
    }

    private void requestOneDayWeatherForecastAndHandleResponse(String apiEndpoint) {
        // Call to network manager to fetch weather
        String weatherResponseString = mNetworkManager.getWeather(apiEndpoint);

        WeatherForecastResponse weatherForecastResponse = null;

        // Attempting weather response parsing
        try {
            if(weatherResponseString != null) {
                weatherForecastResponse = mResponseParser.parseWeatherForecastResponse(weatherResponseString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (weatherForecastResponse != null &&
                Integer.parseInt(weatherForecastResponse.getCod()) == HttpURLConnection.HTTP_OK) {
            onSuccess(weatherForecastResponse);
        } else {
            onFailure();
        }
    }

    /**
     * NOT IMPLEMENTED! Just to show extensibility
     *
     * To make strings constant in WeatherActivity if/when implementing forecasts.
     * Remember to make activity subscribe to these events.
     *
     * @param weatherForecastResponse weather forecast response
     */
    private void onSuccess(WeatherForecastResponse weatherForecastResponse) {
        Logger.i(TAG, "Response successfully received");
        ApplicationModule.getBroadcastManager()
                .sendBroadcast(new Intent("getWeatherForecastSuccessResponseEvent")
                        .putExtra("getWeatherForecastResponseObject",
                                new WeatherForecastSuccessEvent(weatherForecastResponse)));
    }

    /**
     * NOT IMPLEMENTED! Just to show extensibility
     *
     * To make strings constant in WeatherActivity if/when implementing forecasts.
     * Remember to make activity subscribe to these events.
     */
    private void onFailure() {
        Logger.i(TAG, "Response failure");
        ApplicationModule.getBroadcastManager()
                .sendBroadcast(new Intent("getWeatherForecastSuccessResponseEvent")
                        .putExtra("getWeatherForecastResponseObject",
                                new GetWeatherFailureResponseEvent("Getting weather forecast failed")));
    }
}
