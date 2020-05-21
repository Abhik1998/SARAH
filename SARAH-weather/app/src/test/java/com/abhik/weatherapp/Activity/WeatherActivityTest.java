package com.abhik.weatherapp.Activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;

import com.abhik.weatherapp.BuildConfig;
import com.abhik.weatherapp.activity.WeatherActivity;
import com.abhik.weatherapp.fetcher.WeatherFetcher;
import com.abhik.weatherapp.fetcher.WeatherForecastFetcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test class for WeatherActivity
 * TODO: Try developing your own event handling mechanism based on observer pattern as
 * TODO: a mocked LocalBroadcastManager does'nt register and behave as an actual one making testing
 * TODO: for a few parts of the activity impossible.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeatherActivityTest {
    private ActivityController<WeatherActivity> mActivityController;
    private WeatherActivity mActivity;
    @Mock
    private WeatherFetcher mMockWeatherFetcher;

    @Mock
    private WeatherForecastFetcher mMockWeatherForecastFetcher;

    @Mock
    private LocalBroadcastManager mMockLocalBroadcastManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mActivity = new WeatherActivity(mMockWeatherFetcher,
                mMockWeatherForecastFetcher, mMockLocalBroadcastManager);

        // To help drive activity lifecycle
        mActivityController = ActivityController.of(mActivity);
    }

    /**
     * Test sometimes fails when run in test suite but runs perfectly individually.
     * TODO: investigate!
     */
    @Test
    public void testCallToGetWeatherByGeoDataWhenLocationSuccess(){
        //Given
        mActivity = mActivityController.create().start().resume().visible().get();

        // When
        mActivity.onSuccess(new Location("any provider"));

        verify(mMockWeatherFetcher).getWeatherByGeoData(anyString(), anyString());
    }

    @Test
    public void testCallToGetWeatherByCityNameWhenLocationFail(){
        //Given
        mActivity = mActivityController.create().start().resume().visible().get();

        // When
        mActivity.onFailure(new Exception("any exception"));

        // Then
        verify(mMockWeatherFetcher).getWeatherByCityName(anyString());
    }

    @Test
    public void titleIsCorrect() throws Exception {
        // When
        mActivityController.create();

        // Then
        assertThat(mActivity.getTitle().toString(), equalTo("Simple Weather App"));
    }

    @Test
    public void broadcastReceiverIsNotNull() {
        // When
        mActivityController.create();

        // Then
        assertThat(mMockLocalBroadcastManager, notNullValue());
    }

    @Test
    public void testFullScreenFlagIsSet() throws Exception {
        // When
        mActivityController.create();
        //mMockWeatherFetcher.getWeatherByCityName("Tempe");

        // Then
        assertThat(mActivity.getWindow().getAttributes().flags
                & WindowManager.LayoutParams.FLAG_FULLSCREEN, not(0));
    }

    @Test
    public void testRegisterBroadcastReceiverPostOnCreate(){
        // Given

        // When
        mActivityController.create();

        // Then
        verify(mMockLocalBroadcastManager).registerReceiver((BroadcastReceiver) any()
                , (IntentFilter) any());
    }

    @Test
    public void testUnregisterBroadcastReceiverPostOnDestroy(){
        // Given

        // When
        mActivityController.create().destroy();

        // Then
        verify(mMockLocalBroadcastManager, times(1))
                .unregisterReceiver((BroadcastReceiver) any());
    }
}
