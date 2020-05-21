package com.abhik.weatherapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.abhik.weatherapp.fetcher.WeatherFetcher;
import com.abhik.weatherapp.fetcher.WeatherForecastFetcher;
import com.abhik.weatherapp.model.weather.WeatherResponse;
import com.abhik.weatherapp.module.ApplicationModule;
import com.abhik.weatherapp.module.FetcherModule;
import com.anurag.weatherapp.R;
import com.abhik.weatherapp.event.GetWeatherFailureResponseEvent;
import com.abhik.weatherapp.event.GetWeatherSuccessResponseEvent;
import com.abhik.weatherapp.util.BackgroundHelper;
import com.abhik.weatherapp.util.Logger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.abhik.weatherapp.util.BackgroundHelper.getDateAsString;

public class WeatherActivity extends BaseActivity implements OnSuccessListener<Location>,
        OnFailureListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = WeatherActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE_COARSE_LOCATION = 0;
    public static final String GET_WEATHER_EVENT_KEY = "getWeatherResponseEvent";
    public static final String GET_WEATHER_RESPONSE_OBJECT_KEY = "getWeatherResponseObject";

    /**
     * Declaring Views
     */
    private ViewSwitcher mViewSwitcher;
    private TextView mWeatherHeading;
    private TextView mWeatherInCityValue;
    private TextView mWeatherDescriptionValue;
    private TextView mWeatherFeelsLikeValue;
    private TextView mWeatherMinValue;
    private TextView mWeatherMaxValue;
    private TextView mWeatherSunrise;
    private TextView mWeatherSunset;
    private RelativeLayout mWeatherSunriseLayout;
    private RelativeLayout mWeatherSunsetLayout;
    private CoordinatorLayout mCoordinator;
    private ProgressBar mProgressBar;
    private TextView mResponseStatus;

    /**
     * Declaring modules for injection
     * WeatherForecastFetcher has been added to show how can the project be extended to make
     * more network calls which keeping the project easily expandable.
     */
    private final WeatherFetcher mWeatherFetcher;
    private final WeatherForecastFetcher mWeatherForecastFetcher;
    private final LocalBroadcastManager mLocalBroadcastReceiver;

    public WeatherActivity() {
        this(FetcherModule.WeatherFetcher(), FetcherModule.WeatherForecastFetcher(), ApplicationModule.getBroadcastManager());
    }

    public WeatherActivity(WeatherFetcher weatherFetcher,
                           WeatherForecastFetcher weatherForecastFetcher,
                           LocalBroadcastManager localBroadcastManager) {
        mWeatherFetcher = weatherFetcher;
        mWeatherForecastFetcher = weatherForecastFetcher;
        mLocalBroadcastReceiver = localBroadcastManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initializeViews();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mViewSwitcher.setBackgroundResource(BackgroundHelper.getBackground());

        // Registering a receiver to listen to WeatherResponse
        mLocalBroadcastReceiver
                .registerReceiver(weatherMessageReceiver, new IntentFilter(GET_WEATHER_EVENT_KEY));

        // Location permission check
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            // Asks for last known location from the location provider
            getLastLocation();
        }
        Toast.makeText(this, getString(R.string.activity_weather_app_launch_instruction_toast)
                , Toast.LENGTH_LONG).show();
    }

    /**
     * Broadcast receiver to listen to getWeather call's success and failure callbacks
     */
    private BroadcastReceiver weatherMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object responseObject;
            Logger.i(TAG, "Response catched");
            System.out.println("Response catched");
            if (intent != null) {
                responseObject = intent.getParcelableExtra(GET_WEATHER_RESPONSE_OBJECT_KEY);
                if (responseObject instanceof GetWeatherSuccessResponseEvent) {
                    onGetWeatherSuccess((GetWeatherSuccessResponseEvent) responseObject);
                } else if (responseObject instanceof GetWeatherFailureResponseEvent) {
                    onGetWeatherFailure((GetWeatherFailureResponseEvent) responseObject);
                }
            } else {
                onGetWeatherFailure(null);
            }
        }
    };

    /**
     * Binding views
     */
    private void initializeViews() {
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.activity_weather_view_switcher);
        mWeatherHeading = (TextView) findViewById(R.id.activity_weather_text_view_forecast_heading);
        mWeatherInCityValue = (TextView) findViewById(R.id.activity_weather_text_view_in_city_title_value);
        mWeatherDescriptionValue = (TextView) findViewById(R.id.activity_weather_text_view_description_value);
        mWeatherFeelsLikeValue = (TextView) findViewById(R.id.activity_weather_text_view_temp_feels_like_value);
        mWeatherMinValue = (TextView) findViewById(R.id.activity_weather_text_view_temp_min_value);
        mWeatherMaxValue = (TextView) findViewById(R.id.activity_weather_text_view_temp_max_value);
        mWeatherSunrise = (TextView) findViewById(R.id.activity_weather_text_view_sunrise_value);
        mWeatherSunset = (TextView) findViewById(R.id.activity_weather_text_view_sunset_value);
        mWeatherSunriseLayout = (RelativeLayout) findViewById(R.id.activity_weather_layout_sunrise);
        mWeatherSunsetLayout = (RelativeLayout) findViewById(R.id.activity_weather_layout_sunset);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.activity_weather_coordinator);
        mProgressBar = (ProgressBar) findViewById(R.id.activity_weather_progress_bar);
        mResponseStatus = (TextView) findViewById(R.id.activity_weather_text_view_response_status);
    }

    /**
     * * Note: ********
     * Fused location provider only maintains location if at least one other client connected to
     * Location Service requests to retrieve location via 'LocationRequest'. Just turning on the
     * location service is not guaranteed to store last known location. Turning on location service
     * and immediately starting the app might not give enough time for the location to arrive.
     * <p>
     * A sure short way to make sure the location provider has some confirmed location
     * is to launch maps apps first. Launching this app after getting hold of the location will
     * make sure the fusedLocationClient has LastLocation.
     * <p>
     * FusionLocationClient has been used as it is light on battery and recommended by Google.
     * LocationRequest need to be implemented to make sure app has the location when needed
     * and does not have to rely on last known location but hasn't been implemented to keep the app
     * simple.
     */
    private void getLastLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> cityLocation = ApplicationModule.getFusedLocationClient().getLastLocation();
        if (cityLocation != null) {
            cityLocation.addOnSuccessListener(this);
            cityLocation.addOnFailureListener(this);
        } else {
            getWeatherFallback();
        }
    }

    /**
     * Callback for permission result
     *
     * @param requestCode Request code associated with our requested permission
     * @param permissions Array of permissions we asked for
     * @param grantResults Array of permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                getWeatherFallback();
            }
        }
    }

    /**
     * Fallback code when location permission is explicitly denied by the user
     */
    private void getWeatherFallback() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mWeatherFetcher.getWeatherByCityName(getString(R.string.activity_weather_fallback_cityname));
            }
        });
        Snackbar.make(mCoordinator, R.string.activity_weather_weather_fallback_snackbar_copy,
                Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Code to be called when requesting location permission.
     * Shows rationale if needed otherwise the requesting permission dialog
     */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Snackbar.make(mCoordinator, R.string.activity_weather_location_permission_rationale_copy,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.activity_weather_permission_rationale_action_button_copy), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(WeatherActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_CODE_COARSE_LOCATION);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(WeatherActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE_COARSE_LOCATION);
        }
    }

    /**
     * Function which is executed if call to getWeather is successful and get a good
     * weatherResponse object. WeatherResponse object is wrapped in a success event object before
     * being dispatched.
     *
     * @param event Weather Success Response Event object
     */
    private void onGetWeatherSuccess(GetWeatherSuccessResponseEvent event) {
        WeatherResponse response = event.getResponse();
        mViewSwitcher.showNext();
        Log.i(TAG, "Inside success");

        // Filling in data
        mWeatherHeading.setText(String.format("Weather Forecast\n\n%s",
                response.getWeatherList().get(0).getName()));

        //Weather Main
        mWeatherInCityValue.setText(
                response.getWeatherList().get(0).getWeather().get(0).getWeatherMain());

        //Weather Description
        mWeatherDescriptionValue.setText(
                response.getWeatherList().get(0).getWeather().get(0).getWeatherDescription());

        // Temp feels like
        mWeatherFeelsLikeValue.setText(String.format("%s",
                Double.toString(response.getWeatherList().get(0).getMain().getTemp())));

        // min Temp
        mWeatherMinValue.setText(String.format("%s",
                Double.toString(response.getWeatherList().get(0).getMain().getMinTemp())));

        //Max temp
        mWeatherMaxValue.setText(String.format("%s",
                Double.toString(response.getWeatherList().get(0).getMain().getMaxTemp())));

        // Sunrise
        Long sunrise = response.getWeatherList().get(0).getSys().getSunrise();
        if (sunrise != 0L) {
            mWeatherSunriseLayout.setVisibility(View.VISIBLE);
            mWeatherSunrise.setText(getDateAsString(response.getWeatherList().get(0)
                    .getSys().getSunrise()));
        } else {
            mWeatherSunriseLayout.setVisibility(View.GONE);
        }

        // Sunset
        Long sunset = response.getWeatherList().get(0).getSys().getSunset();
        if (sunset != 0L) {
            mWeatherSunsetLayout.setVisibility(View.VISIBLE);
            mWeatherSunset.setText(getDateAsString(response.getWeatherList().get(0)
                    .getSys().getSunset()));
        } else {
            mWeatherSunsetLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Function which is executed if call to getWeather fails or we get a bad
     * weatherResponse object. WeatherResponse object is wrapped in a failed event object before
     * being dispatched.
     *
     * @param event Weather Failed Response Event object
     */
    private void onGetWeatherFailure(GetWeatherFailureResponseEvent event) {
        if (event != null) {
            Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.activity_weather_weather_fetch_failure_toast_copy
                    , Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
        mResponseStatus.setText(R.string.activity_weather_failure_response_copy);
    }

    /**
     * Success callback of location listener called when location provider gets a successful
     * catch of the location.
     *
     * @param location Location object from location provider
     */
    @Override
    public void onSuccess(final Location location) {
        if (location != null) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    mWeatherFetcher.getWeatherByGeoData(String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude()));
                }
            });
        }
    }

    /**
     * Failure callback called when location provider fails to get location.
     *
     * @param e exception object
     */
    @Override
    public void onFailure(@NonNull Exception e) {
        getWeatherFallback();
    }

    /**
     * onDestory() activity lifecycle event callback
     */
    @Override
    protected void onDestroy() {
        mLocalBroadcastReceiver.unregisterReceiver(weatherMessageReceiver);
        super.onDestroy();
    }

    /**
     * Added to show easy extendability of the project
     */
    /*private void onGetWeatherForecastSuccess(WeatherForecastSuccessEvent event) {
        Logger.i(TAG, "Weather forecast success");
    }

    private void onGetWeatherForecastFailure(WeatherForecastFailureResponse event) {
        Logger.i(TAG, "Weather forecast failure");
    }*/
}
