package com.aleksandarbroker.insurance;

/**
 * Created by Martin on 5/18/2016.
 */

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
import android.webkit.WebView;

import com.aleksandarbroker.insurance.functions.NVP;

import java.util.ArrayList;

public class Privacy extends AppCompatActivity {

    private String pass;
    private ArrayList<NVP> postdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent i = getIntent();
        pass = i.getStringExtra("pass");
        postdata = (ArrayList<NVP>) i.getSerializableExtra("data");
        WebView wv = (WebView) findViewById(R.id.webView1);
        wv.loadUrl("file:///android_asset/privacy.html");

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

        Intent i = new Intent(Privacy.this, HomeScreen.class);
        i.putExtra("pass", pass);
        i.putExtra("data", postdata);
        i.putExtra("from", "pr");
        startActivity(i);
        finish();

    }

}
