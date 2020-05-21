package com.abhik.weatherapp.fetcher;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;

import com.abhik.weatherapp.model.weather.WeatherResponse;
import com.anurag.weatherapp.R;
import com.abhik.weatherapp.event.GetWeatherFailureResponseEvent;
import com.abhik.weatherapp.event.GetWeatherSuccessResponseEvent;
import com.abhik.weatherapp.network.NetworkManager;
import com.abhik.weatherapp.network.OpenWeatherMapResponseParser;
import com.abhik.weatherapp.util.Logger;

import org.json.JSONException;

import java.net.HttpURLConnection;

import static com.abhik.weatherapp.activity.WeatherActivity.GET_WEATHER_EVENT_KEY;
import static com.abhik.weatherapp.activity.WeatherActivity.GET_WEATHER_RESPONSE_OBJECT_KEY;

/**
 * Implementation class for getting weather info
 */
public class WeatherNetworkFetcher implements WeatherFetcher, Callback<WeatherResponse> {
    private static final String TAG = WeatherNetworkFetcher.class.getSimpleName();
    private final NetworkManager mNetworkManager;
    private final OpenWeatherMapResponseParser mWeatherResponseParser;
    private final Resources mResources;
    private final LocalBroadcastManager mLocalBroadcastManager;

    public WeatherNetworkFetcher(NetworkManager networkManager,
                                 OpenWeatherMapResponseParser responseParser,
                                 Resources resources,
                                 LocalBroadcastManager localBroadcastManager) {
        this.mNetworkManager = networkManager;
        this.mWeatherResponseParser = responseParser;
        this.mResources = resources;
        this.mLocalBroadcastManager = localBroadcastManager;
    }

    public void getWeatherByGeoData(String lat, String lon) {
        String OPEN_WEATHER_MAP_GET_WEATHER_BY_GEO_DATA_ENDPOINT = "/data/2.5/find?lat=%s&lon=%s&cnt=1&appid=%s";
        String apiEndpoint = String.format(OPEN_WEATHER_MAP_GET_WEATHER_BY_GEO_DATA_ENDPOINT,
                lat, lon, mResources.getString(R.string.weather_open_weather_map_appid));

        requestWeatherAndHandleResponse(apiEndpoint);
    }

    public void getWeatherByCityName(String cityName) {
        String OPEN_WEATHER_MAP_GET_WEATHER_BY_CITY_NAME_ENDPOINT = "/data/2.5/find?appid=%s&q=%s";
        String apiEndpoint = String.format(OPEN_WEATHER_MAP_GET_WEATHER_BY_CITY_NAME_ENDPOINT,
                mResources.getString(R.string.weather_open_weather_map_appid),
                cityName);

        // Call to network manager to fetch weather by cityName
        requestWeatherAndHandleResponse(apiEndpoint);
    }

    private void requestWeatherAndHandleResponse(String apiEndpoint) {
        // Call to network manager to fetch weather
        String weatherResponseString = mNetworkManager.getWeather(apiEndpoint);

        WeatherResponse weatherResponse = null;

        // Attempting weather response parsing
        try {
            if(weatherResponseString != null) {
                weatherResponse = mWeatherResponseParser.parseWeatherResponse(weatherResponseString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (weatherResponse != null &&
                Integer.parseInt(weatherResponse.getCod()) == HttpURLConnection.HTTP_OK) {
            onSuccess(weatherResponse);
        } else {
            onFailure();
        }
    }

    /**
     * Success callback for GetWeatherResponse
     *
     * @param weatherResponse GetWeatherSuccessResponse
     */
    @Override
    public void onSuccess(WeatherResponse weatherResponse) {
        Logger.i(TAG, "Response successfully received");
        mLocalBroadcastManager
                .sendBroadcast(new Intent(GET_WEATHER_EVENT_KEY)
                        .putExtra(GET_WEATHER_RESPONSE_OBJECT_KEY,
                                new GetWeatherSuccessResponseEvent(weatherResponse)));
    }

    /**
     * Failure callback of GetWeatherresponse
     */
    @Override
    public void onFailure() {
        Logger.i(TAG, "Response failure");
        mLocalBroadcastManager
                .sendBroadcast(new Intent(GET_WEATHER_EVENT_KEY)
                        .putExtra(GET_WEATHER_RESPONSE_OBJECT_KEY,
                                new GetWeatherFailureResponseEvent("Get weather failure response received")));
    }
}

