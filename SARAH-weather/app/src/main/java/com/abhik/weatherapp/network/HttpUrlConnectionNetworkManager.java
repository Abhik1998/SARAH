package com.abhik.weatherapp.network;

import android.content.res.Resources;

import com.abhik.weatherapp.R;
import com.abhik.weatherapp.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *  NetworkManager implementation class.
 */

public class HttpUrlConnectionNetworkManager implements NetworkManager {
    private static final String TAG = HttpUrlConnectionNetworkManager.class.getSimpleName();
    private final Resources mResources;
    private HttpURLConnection mHttpClient = null;

    public HttpUrlConnectionNetworkManager(Resources resources){
        this.mResources = resources;
    }

    public String getWeather(String apiEndpoint) {
        try {
            if(apiEndpoint == null || apiEndpoint.equals("")){
                throw new MalformedURLException("Api endpoint cannot be null or blank");
            }
            URL mUrl = new URL(mResources.getString(R.string.api_base_url) + apiEndpoint);
            mHttpClient = (HttpURLConnection) mUrl.openConnection();

            //Setting parameters
            mHttpClient.setRequestMethod("GET");
            mHttpClient.setDoOutput(true);
            mHttpClient.connect();

            // Reading the response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(mHttpClient.getInputStream()));

            StringBuilder jsonString = new StringBuilder();
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                jsonString.append(tmp).append("\n");
            }
            reader.close();

            return jsonString.toString();
        } catch (MalformedURLException e) {
            Logger.e(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            return null;
        } finally {
            if (mHttpClient != null) {
                mHttpClient.disconnect();
            }
        }
    }
}
