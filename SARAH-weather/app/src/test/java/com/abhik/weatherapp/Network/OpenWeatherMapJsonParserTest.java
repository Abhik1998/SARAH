package com.abhik.weatherapp.Network;

import com.abhik.weatherapp.Util.TestResourceReaderUtil;
import com.abhik.weatherapp.model.weather.WeatherConditions;
import com.abhik.weatherapp.model.weather.WeatherMain;
import com.abhik.weatherapp.model.weather.WeatherParams;
import com.abhik.weatherapp.model.weather.WeatherResponse;
import com.abhik.weatherapp.model.weather.WeatherSys;
import com.abhik.weatherapp.network.OpenWeatherMapJsonResponseParser;
import com.abhik.weatherapp.network.OpenWeatherMapResponseParser;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Test class OpenWeatherMapJsonParserTest
 */

@RunWith(RobolectricTestRunner.class)
public class OpenWeatherMapJsonParserTest {
    private OpenWeatherMapResponseParser mJsonResponseParser;
    private ClassLoader classLoader;

    private WeatherResponse correctWeatherResponse = new WeatherResponse.Builder()
            .mCod("200").mAccuracy("accurate")
            .mWeatherList(new ArrayList<WeatherParams>() {{
                add(new WeatherParams.Builder()
                        .mName("Tempe")
                        .mMain(new WeatherMain.Builder().mTemp(288.81)
                                .mMaxTemp(290.15).mMinTemp(288.15).build())
                        .mWeather(new ArrayList<WeatherConditions>() {{
                            add(new WeatherConditions.Builder().mWeatherDescription("scattered clouds")
                                    .mWeatherMain("Clouds").mWeatherIcon("03n").build());
                        }})
                        .mSys(new WeatherSys.Builder().mSunset(0L).mSunrise(0L).build())
                        .build());
            }}).build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mJsonResponseParser = new OpenWeatherMapJsonResponseParser();
        classLoader = this.getClass().getClassLoader();
    }

    @Test
    public void testJsonFileLoading() throws JSONException {
        // Given
        String loadedJsonString = TestResourceReaderUtil.readFile(classLoader,
                "get_weather_success_response.json");

        // When
        WeatherResponse response = mJsonResponseParser.parseWeatherResponse(loadedJsonString);

        // Then
        assertThat(loadedJsonString, instanceOf(String.class));
    }

    @Test
    public void testWeatherObjectCreation() throws JSONException {
        // Given
        String loadedJsonString = TestResourceReaderUtil.readFile(classLoader,
                "get_weather_success_response.json");

        // When
        WeatherResponse response = mJsonResponseParser.parseWeatherResponse(loadedJsonString);

        // Then
        assertThat(response, instanceOf(WeatherResponse.class));
    }

    @Test
    public void testWeatherResponseParsing_correctInput() throws JSONException {
        // Given
        String loadedJsonString = TestResourceReaderUtil.readFile(classLoader,
                "get_weather_success_response.json");

        // When
        WeatherResponse response = mJsonResponseParser.parseWeatherResponse(loadedJsonString);

        // Then
        // TODO: Reflection based object comparison not working, to investigate
        //assertThat(correctWeatherResponse, samePropertyValuesAs(response));

        assertThat(correctWeatherResponse.getAccuracy(), equalTo(response.getAccuracy()));
        assertThat(correctWeatherResponse.getCod(), equalTo(response.getCod()));
        assertThat(correctWeatherResponse.getCityCount(), equalTo(response.getCityCount()));

        assertThat(correctWeatherResponse.getWeatherList().get(0).getName(),
                equalTo(response.getWeatherList().get(0).getName()));

        assertThat(correctWeatherResponse.getWeatherList().get(0).getMain().getTemp(),
                equalTo(response.getWeatherList().get(0).getMain().getTemp()));
        assertThat(correctWeatherResponse.getWeatherList().get(0).getMain().getMaxTemp(),
                equalTo(response.getWeatherList().get(0).getMain().getMaxTemp()));
        assertThat(correctWeatherResponse.getWeatherList().get(0).getMain().getMinTemp(),
                equalTo(response.getWeatherList().get(0).getMain().getMinTemp()));

        assertThat(correctWeatherResponse.getWeatherList().get(0).getSys().getSunrise(),
                equalTo(response.getWeatherList().get(0).getSys().getSunrise()));
        assertThat(correctWeatherResponse.getWeatherList().get(0).getSys().getSunset(),
                equalTo(response.getWeatherList().get(0).getSys().getSunset()));

        assertThat(correctWeatherResponse.getWeatherList().get(0).getWeather().get(0).getWeatherMain(),
                equalTo(response.getWeatherList().get(0).getWeather().get(0).getWeatherMain()));
        assertThat(correctWeatherResponse.getWeatherList().get(0).getWeather().get(0).getWeatherDescription(),
                equalTo(response.getWeatherList().get(0).getWeather().get(0).getWeatherDescription()));
        assertThat(correctWeatherResponse.getWeatherList().get(0).getWeather().get(0).getWeatherIcon(),
                equalTo(response.getWeatherList().get(0).getWeather().get(0).getWeatherIcon()));
    }

    /**
     * Additional check in WeatherNetworkFetcher to make sure null string is not given for parsing
     *
     * @throws JSONException Exception
     */
    @Test(expected = JSONException.class)
    public void testWeatherResponseParsing_NullInput() throws JSONException {
        // Given
        String loadedJsonString = null;

        // When
        WeatherResponse response = mJsonResponseParser.parseWeatherResponse(loadedJsonString);

        //Then Returns null and throws JSONExpection
        assertThat(response, equalTo(null));
    }

    @Test(expected = JSONException.class)
    public void testWeatherResponseParsing_EmptyStringInput() throws JSONException {
        // Given
        String loadedJsonString = "";

        // When
        WeatherResponse response = mJsonResponseParser.parseWeatherResponse(loadedJsonString);

        // Then Returns null and throws JSONExpection
        assertThat(response, equalTo(null));
    }

    @Test(expected = NullPointerException.class)
    public void testWeatherResponseParsing_incompleteInput_NoSysObject() throws JSONException {
        // Given
        String loadedJsonString = TestResourceReaderUtil.readFile(classLoader,
                "get_weather_success_no_weather_sys.json");

        // When
        WeatherResponse response = mJsonResponseParser.parseWeatherResponse(loadedJsonString);

        // Then throws NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void testWeatherResponseParsing_incompleteInput_NoWeatherListArray() throws JSONException {
        // Given
        String loadedJsonString = TestResourceReaderUtil.readFile(classLoader,
                "get_weather_success_no_list_array.json");

        // When
        WeatherResponse response = mJsonResponseParser.parseWeatherResponse(loadedJsonString);

        // Then throws NullPointerException
    }

}
