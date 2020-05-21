# Simple Weather App [![Build status](https://build.appcenter.ms/v0.1/apps/1ed1ef63-6202-4132-9124-232c3ddff5d9/branches/master/badge)](https://appcenter.ms)
This is a simple weather app which gives used the current weather at his/her location provided
the location service is turned on. The app takes care of permissions and if location permission is
 denied it loads weather at my current Location Tempe, AZ!

I tried to keep the code extensible by using interfaces and dependency injection.
Along with weather network call to OpenWeatherApi, the weather forecast network call has also
 been implemented but not used/called in the weatherActivity to showcase this extensibility.

I have marked some issues for myself to investigate/study on as TODO.
The code has been unit tested and and tests can be found under ~app/src/test.
The WeatherActivity can be tested a bit further but I have kept that as an exercise for myself
 after I resolve the TODO items.

I look forward to your feedback and comments on the code, architecture improvements and/or any
 general suggestions.

#### Getting started

#### 1. Open the project in `Android Studio`

Select `Open Existing Project` from the options. To install `Android Studio` please follow the instructions [here](https://developer.android.com/sdk/installing/studio.html)

#### 2. Running project on device

After the project has been successfully imported it will build itself automatically.
Connect the mobile device and press the `Run 'app'` button from `Run menu`.
Please also note the `min supported API version is Android 4.3 Jelly Bean`.

More instructions [here](https://developer.android.com/training/basics/firstapp/running-app.html)

### Formatting rules followed:
* Column width 100
* Field instance members are prefixed with 'm' e.g. mTextView
* Static members are prefixed with 's'
* Constants are private (unless needed elsewhere), static and all in uppercase, e.g. FADE_ANIMATION_OUT_MILLIS

### Naming conventions

String ids:

`<activity/fragment/view/global>_<activity_name>__<string name>` e.g. activity_home_welcome_message

If a string is used in multiple places, use the 'global' prefix rather than the screen name.

Layout ids:

`<activity/fragment/view>_<activity_name>_<type>_<description>` e.g. activity_home_textview_welcome_message

### Third-party Libraries used for testing
* `JUnit` - https://github.com/junit-team/junit4
* `Mockito` - https://github.com//mockito/mockito
* `Hamcrest` - https://github.com/hamcrest/JavaHamcrest
* `Robolectric` - https://github.com/robolectric/robolectric
