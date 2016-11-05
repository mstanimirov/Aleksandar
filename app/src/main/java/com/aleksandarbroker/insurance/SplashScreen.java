package com.aleksandarbroker.insurance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aleksandarbroker.insurance.functions.Functions;
import com.aleksandarbroker.insurance.functions.NVP;

import java.util.ArrayList;

public class SplashScreen extends Activity {

    private static final int SPLASH_TIME_OUT = 2000;
    Functions Functions = new Functions(this);
    private ArrayList<NVP> postdata = new ArrayList<NVP>(1);

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //getSharedPref();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                Intent i = new Intent(SplashScreen.this, HomeScreen.class);

                postdata.add(new NVP("FirstName", ""));
                postdata.add(new NVP("MidName", ""));
                postdata.add(new NVP("LastName", ""));
                postdata.add(new NVP("EGN", ""));
                postdata.add(new NVP("MobileNumber", ""));
                postdata.add(new NVP("Email", ""));
                postdata.add(new NVP("LegalEntity", ""));
                postdata.add(new NVP("Bulstat", ""));
                postdata.add(new NVP("FirmName", ""));
                postdata.add(new NVP("FirmAddress", ""));
                postdata.add(new NVP("City", ""));
                postdata.add(new NVP("Address", ""));
                postdata.add(new NVP("Type", ""));
                postdata.add(new NVP("Model", ""));
                postdata.add(new NVP("RegNo", ""));
                postdata.add(new NVP("RamaNo", ""));
                postdata.add(new NVP("Date", ""));
                postdata.add(new NVP("Places", ""));
                postdata.add(new NVP("Insurer", ""));
                postdata.add(new NVP("PolicyType", ""));
                postdata.add(new NVP("InstallmentPlan", ""));
                postdata.add(new NVP("GreenCard", ""));
                postdata.add(new NVP("FirstNameLat", ""));
                postdata.add(new NVP("MidNameLat", ""));
                postdata.add(new NVP("LastNameLat", ""));

                i.putExtra("data", postdata);
                i.putExtra("from", "ss");
                startActivity(i);
                finish();

                //overridePendingTransition(R.anim.popup_hide,R.anim.popup_show);
            }
        }, SPLASH_TIME_OUT);

    }

}
