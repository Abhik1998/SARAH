package com.abhik.weatherapp.Fetcher;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;

import com.anurag.weatherapp.R;
import com.abhik.weatherapp.event.GetWeatherFailureResponseEvent;
import com.abhik.weatherapp.event.GetWeatherSuccessResponseEvent;
import com.abhik.weatherapp.fetcher.WeatherNetworkFetcher;
import com.abhik.weatherapp.model.weather.WeatherResponse;
import com.abhik.weatherapp.network.NetworkManager;
import com.abhik.weatherapp.network.OpenWeatherMapResponseParser;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.abhik.weatherapp.activity.WeatherActivity.GET_WEATHER_RESPONSE_OBJECT_KEY;
import static com.abhik.weatherapp.module.ApplicationModule.applicationContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Test class for WeatherNetworkFetcher.
 */
@RunWith(RobolectricTestRunner.class)
public class GetWeatherNetworkFetcherTest {
    private final String BY_CITY_NAME_STRING =
            "/data/2.5/find?appid=721cb50cc767c368b9839f30e803df39&q=Tempe";
    private static final String A_STRING = "WEATHER_JSON";
    private final WeatherResponse BAD_WEATHER_RESPONSE_OBJECT = new WeatherResponse.Builder()
            .mCod("100").build();
    private final WeatherResponse GOOD_WEATHER_RESPONSE_OBJECT = new WeatherResponse.Builder()
            .mCod("200").build();

    @Mock
    private NetworkManager mMockNetworkManager;

    @Mock
    private OpenWeatherMapResponseParser mMockOpenWeatherMapResponseParser;

    @Mock
    private Resources mMockResources;

    @Mock
    private LocalBroadcastManager mMockLocalBroadcastManager;

    private WeatherNetworkFetcher mWeatherNetworkFetcher;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mWeatherNetworkFetcher = new WeatherNetworkFetcher(mMockNetworkManager,
                mMockOpenWeatherMapResponseParser,
                mMockResources, mMockLocalBroadcastManager);

        // Stubbing response for appID from resources as part of setup
        when(mMockResources.getString(R.string.weather_open_weather_map_appid))
                .thenReturn(applicationContext().getString(R.string.weather_open_weather_map_appid));
    }

    @Test
    public void testGetWeatherByCityNameNetworkManagerInvocationTest() {
        // Given resources return the correct endpoint

        // When
        mWeatherNetworkFetcher.getWeatherByCityName("Tempe");

        // Then
        verify(mMockNetworkManager, times(1))
                .getWeather(eq(BY_CITY_NAME_STRING));
    }

    @Test
    public void testGetWeatherByGeoDataNetworkManagerInvocationTest() {
        // Given resources return the correct endpoint

        // When
        mWeatherNetworkFetcher.getWeatherByGeoData("25", "36");

        // Then
        String BY_GEO_DATA_ENDPOINT = "/data/2.5/find?lat=25&lon=36&cnt=1&appid=721cb50cc767c368b9839f30e803df39";
        verify(mMockNetworkManager, times(1))
                .getWeather(eq(BY_GEO_DATA_ENDPOINT));
    }

    @Test()
    public void testJsonParserInteraction_nullResponse1() {
        // Given
        when(mMockNetworkManager.getWeather(anyString())).thenReturn("");

        // When
        mMockNetworkManager.getWeather("");

        // Then
        verifyZeroInteractions(mMockOpenWeatherMapResponseParser);
    }

    @Test()
    public void testJsonParserInteraction_nullResponse2() {
        // Given
        when(mMockNetworkManager.getWeather(anyString())).thenReturn(null);

        // When
        mMockNetworkManager.getWeather(null);

        // Then
        verifyZeroInteractions(mMockOpenWeatherMapResponseParser);
    }

    @Test
    public void testJsonParserInteraction_nullResponse3() throws JSONException {
        // Given
        when(mMockNetworkManager.getWeather(BY_CITY_NAME_STRING)).thenReturn(A_STRING);

        // When
        mWeatherNetworkFetcher.getWeatherByCityName("Tempe");

        // Then
        verify(mMockOpenWeatherMapResponseParser, times(1))
                .parseWeatherResponse(A_STRING);
    }

    @Test
    public void testCallbackInteraction_onReceivingNon200Code() throws JSONException {
        // Given
        when(mMockNetworkManager.getWeather(BY_CITY_NAME_STRING)).thenReturn(A_STRING);
        when(mMockOpenWeatherMapResponseParser.parseWeatherResponse(A_STRING))
                .thenReturn(BAD_WEATHER_RESPONSE_OBJECT);

        // When
        mWeatherNetworkFetcher.getWeatherByCityName("Tempe");

        // Then
        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(mMockLocalBroadcastManager, times(1))
                .sendBroadcast(argument.capture());
        Object expected = argument.getValue()
                .getParcelableExtra(GET_WEATHER_RESPONSE_OBJECT_KEY);
        assertThat(expected, instanceOf(GetWeatherFailureResponseEvent.class));
    }

    @Test
    public void testCallbackInteraction_onReceiving200Code() throws JSONException {
        // Given
        when(mMockNetworkManager.getWeather(BY_CITY_NAME_STRING)).thenReturn(A_STRING);
        when(mMockOpenWeatherMapResponseParser.parseWeatherResponse(A_STRING))
                .thenReturn(GOOD_WEATHER_RESPONSE_OBJECT);

        // When
        mWeatherNetworkFetcher.getWeatherByCityName("Tempe");

        // Then
        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(mMockLocalBroadcastManager, times(1))
                .sendBroadcast(argument.capture());
        Object expected = argument.getValue()
                .getParcelableExtra(GET_WEATHER_RESPONSE_OBJECT_KEY);
        assertThat(expected, instanceOf(GetWeatherSuccessResponseEvent.class));
    }
}
