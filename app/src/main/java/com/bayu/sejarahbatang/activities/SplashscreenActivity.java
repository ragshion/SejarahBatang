package com.bayu.sejarahbatang.activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.maps.MapActivity;


public class SplashscreenActivity extends AppCompatActivity {

    MaterialDialog materialDialog;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashscreenActivity.this, MapActivity.class);
            SplashscreenActivity.this.startActivity(intent);
            SplashscreenActivity.this.finish();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }



}

