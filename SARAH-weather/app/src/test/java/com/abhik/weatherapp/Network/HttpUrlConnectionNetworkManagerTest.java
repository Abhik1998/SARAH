package com.abhik.weatherapp.Network;

import android.content.res.Resources;

import com.anurag.weatherapp.R;
import com.abhik.weatherapp.model.weather.WeatherResponse;
import com.abhik.weatherapp.network.HttpUrlConnectionNetworkManager;
import com.abhik.weatherapp.network.NetworkManager;
import com.abhik.weatherapp.network.OpenWeatherMapJsonResponseParser;
import com.abhik.weatherapp.network.OpenWeatherMapResponseParser;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.abhik.weatherapp.module.ApplicationModule.applicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

/**
 * Test class for HttpUrlConnection
 */

@RunWith(RobolectricTestRunner.class)
public class HttpUrlConnectionNetworkManagerTest {
    private NetworkManager mNetworkManager;
    private final String BY_CITY_NAME_STRING =
            "/data/2.5/find?appid=721cb50cc767c368b9839f30e803df39&q=Tempe";

    @Mock
    private Resources mResources;

    private OpenWeatherMapResponseParser mJsonResponseParser;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mNetworkManager = new HttpUrlConnectionNetworkManager(mResources);

        // stubbing for the defaultCase
        when(mResources.getString(R.string.api_base_url))
                .thenReturn(applicationContext().getString(R.string.api_base_url));
        mJsonResponseParser = new OpenWeatherMapJsonResponseParser();
    }

    @Test
    public void testUrlFormation_correctStringReturn(){
        // Given Resources return the correct base url

        // When
        String response = mNetworkManager.getWeather(BY_CITY_NAME_STRING);

        // Then
        assertThat(response, instanceOf(String.class));
    }

    @Test
    public void testReturnedStringDeserialization_withCorrectUrl() throws JSONException {
        // Given resources return the correct base url

        // When
        String response = mNetworkManager.getWeather(BY_CITY_NAME_STRING);

        // Then
        assertThat(mJsonResponseParser.parseWeatherResponse(response), instanceOf(WeatherResponse.class));
    }

    @Test
    public void testMalformedString_EndpointNull_OutputNull() throws JSONException {
        // Given resources return the correct base url

        // When
        String response = mNetworkManager.getWeather(null);

        // Then
        assertThat(response, equalTo(null));
    }

    @Test
    public void testMalformedString_EndpointEmpty_OutputNull() throws JSONException {
        // Given resources return the correct base url

        // When
        String response = mNetworkManager.getWeather("");

        // Then
        assertThat(response, equalTo(null));
    }

    @Test
    public void testMalformedString_IncorrectProtocol_OutputNull() throws JSONException {
        // Given
        when(mResources.getString(R.string.api_base_url))
                .thenReturn("api.openweathermap.org");

        // When
        String response = mNetworkManager.getWeather(BY_CITY_NAME_STRING);

        // Then causes MalformedUrlException and returns null
        assertThat(response, equalTo(null));
    }

    @Test
    public void testMalformedString_IncorrectBaseUrl_OutputNull() throws JSONException {
        // Given
        when(mResources.getString(R.string.api_base_url))
                .thenReturn("http://api.openweathermap");

        // When
        String response = mNetworkManager.getWeather(BY_CITY_NAME_STRING);

        // Then causes IOException and returns null
        assertThat(response, equalTo(null));
    }


}
