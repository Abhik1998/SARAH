package com.triointeli.sarah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.triointeli.sarah.WatBot.Boarding;

import org.w3c.dom.Text;

import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private TextView textSplashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textSplashScreen=(TextView)findViewById(R.id.id_splashScreenTextView);

        //creating the list of quotes to be randomly displayed each time app is launched
        String sarahSpeakList[] = {
                "I will take care of your time ...\nSarah is your friend !",
                "Can you imagine my smile ...",
                "Hi there! \n Sarah is glad to see you !!!",
                "Hey...\n How are you !"

        };

        //random number generator
        Random rand = new Random();

        //4 is the maximum and 0 is the minimum
        int  n = rand.nextInt(4);

        //displaying the quote at index 'n' in the text view
        String sarahSpeak = sarahSpeakList[n];
        textSplashScreen.setText(sarahSpeak);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Starts login activity
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

                if (!sharedPreferences.getBoolean(
                        "Boarding Complete", false)) {
                    startActivity(new Intent(SplashActivity.this, Boarding.class));
                    sharedPreferencesEditor.putBoolean(
                            "Boarding Complete", true);
                    sharedPreferencesEditor.apply();
                }
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // close splash activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
