package com.aleksandarbroker.insurance;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aleksandarbroker.insurance.functions.NVP;

import java.util.ArrayList;

public class About extends AppCompatActivity {

    private String pass;
    private ArrayList<NVP> postdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent i = getIntent();
        pass = i.getStringExtra("pass");
        postdata = (ArrayList<NVP>) i.getSerializableExtra("data");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Redirecting...", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                final int seconds = 2;
                final int milliseconds = seconds * 1000;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getResources().getString(R.string.packageName))));

                            }
                        }, milliseconds);
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(About.this, HomeScreen.class);
        i.putExtra("pass", pass);
        i.putExtra("from", "ab");
        i.putExtra("data", postdata);
        startActivity(i);
        finish();

    }

}
