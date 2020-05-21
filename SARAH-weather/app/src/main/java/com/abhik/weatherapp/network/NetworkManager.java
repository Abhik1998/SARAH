package com.abhik.weatherapp.network;

/**
 *  NetworkManager interface
 */
public interface NetworkManager {
    /**
     * A single call is being used due to the similarity of request.
     * Can be easily extended to accomodate more requests.
     *
     * @param apiEndpoint Formatted API endpoint to be appended to base Url before making a call.
     * @return jsonString or null for success and failure response respectively.
     */
    String getWeather(final String apiEndpoint);
}
