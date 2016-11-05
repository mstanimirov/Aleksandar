package com.aleksandarbroker.insurance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aleksandarbroker.insurance.functions.CustomDialog;
import com.aleksandarbroker.insurance.functions.Functions;
import com.aleksandarbroker.insurance.functions.NVP;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    Functions Functions = new Functions(this);

    String APP_PNAME;

    private ImageButton quote;
    private ImageButton privacy;
    private ImageButton about;
    private TextView rateus;
    private String activity;
    private ArrayList<NVP> postdata = new ArrayList<NVP>(1);

    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        getWindow().setBackgroundDrawableResource(R.drawable.bg_eco);

        //MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713"); //Google Admob Test Banner
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6977436140360247~5731372010");

        Intent i = getIntent();
        postdata = (ArrayList<NVP>) i.getSerializableExtra("data");
        String from = i.getStringExtra("from");

        quote = (ImageButton) findViewById(R.id.Quote);
        privacy = (ImageButton) findViewById(R.id.Privacy);
        about = (ImageButton) findViewById(R.id.About);

        setListeners();

        AdRequest adRequest = new AdRequest.Builder().build();

        if (from.equals("ta")) {

            // Prepare the Interstitial Ad
            interstitial = new InterstitialAd(HomeScreen.this);
// Insert the Ad Unit ID
            interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

            interstitial.loadAd(adRequest);
// Prepare an Interstitial Ad Listener
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    // Call displayInterstitial() function
                    displayInterstitial();
                }
            });

        }else{

            AdView mAdView = (AdView) findViewById(R.id.adView);
            mAdView.loadAd(adRequest);

        }

    }

    public void displayInterstitial() {

        if (interstitial.isLoaded()) {

            interstitial.show();

        }
    }

    public void setListeners() {

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class<?> SendTo = MainActivity.class;

                OnClickStartAnimation(0, SendTo);

            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OnClickStartAnimation(0, Privacy.class);

            }

        });

        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                OnClickStartAnimation(0, About.class);

            }

        });
    }

    public void OnClickStartAnimation(int seconds, final Class<?> where) {
        final int milliseconds = seconds * 1000;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(HomeScreen.this, where);
                        i.putExtra("data", postdata);
                        startActivity(i);
                        finish();

                        //overridePendingTransition(R.anim.popup_hide,null);
                    }
                }, milliseconds);
            }
        });
    }

    public void showToast(String str) {

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {

        final CustomDialog.Builder dialog = new CustomDialog.Builder(this);

        dialog.setTitle("Изход");
        dialog.setMessage("Сигурни ли сте, че искате да излезете?");
        dialog.addButtonGroup(this, "Да", new View.OnClickListener() {

            public void onClick(View v) {

                dialog.dismiss();
                finish();
                System.exit(1);

            }
        });
        dialog.addButtonGroup(this, "Не", new View.OnClickListener() {

            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        dialog.show();

    }

}
