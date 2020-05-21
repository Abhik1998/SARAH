package com.abhik.weatherapp.network;

import com.abhik.weatherapp.model.weather.WeatherForecastResponse;
import com.abhik.weatherapp.model.weather.WeatherResponse;

import org.json.JSONException;

/**
 * Interface to be implemented by JSONParser implementation
 */

public interface OpenWeatherMapResponseParser {
    /**
     * Deserializes WeatherResponse json string to WeatherResponse Object
     *
     * @param jsonString
     * @return
     * @throws JSONException
     */
    WeatherResponse parseWeatherResponse(String jsonString) throws JSONException;

    /**
     * Function to facilitate parsing of Weather Forecast response json string
     * !NOT IMPLEMENTED!
     *
     * @param jsonString Weather forecast reponse json string
     * @return WeatherForecastResponse object
     * @throws JSONException
     */
    WeatherForecastResponse parseWeatherForecastResponse(String jsonString) throws JSONException;
}
