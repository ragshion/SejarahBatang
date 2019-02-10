package com.bayu.sejarahbatang.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bayu.sejarahbatang.R;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SliderLayout sliderLayout;

    private boolean doubleBackToExitPressedOnce = false;

    CardView cardBangunan, cardMasjid, cardMakam, cardGoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqPermitGps();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        this.setTitle("");

        sliderLayout = findViewById(R.id.imageslider);

        HashMap<String,String> url_maps = new HashMap<String, String>();

        url_maps.put("Tes 1", "http://arjati.untungsupriyono.web.id/uploads/sentiling.jpg");
        url_maps.put("Tes 2", "http://arjati.untungsupriyono.web.id/uploads/MasjidKauman.jpg");
        url_maps.put("Tes 3", "http://arjati.untungsupriyono.web.id/uploads/petaPekalongan.jpg");

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(5000);
        sliderLayout.startAutoCycle();

        cardBangunan = findViewById(R.id.cardBangunan);
        cardGoa = findViewById(R.id.cardGoa);
        cardMakam = findViewById(R.id.cardMakam);
        cardMasjid = findViewById(R.id.cardMasjid);

        cardBangunan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bangunan = new Intent(MainActivity.this, DataActivity.class);
                bangunan.putExtra("judul","Bangunan");
                startActivity(bangunan);
            }
        });

        cardGoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Goa = new Intent(MainActivity.this, DataActivity.class);
                Goa.putExtra("judul","Goa");
                startActivity(Goa);
            }
        });

        cardMakam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Makam = new Intent(MainActivity.this, DataActivity.class);
                Makam.putExtra("judul","Makam");
                startActivity(Makam);
            }
        });

        cardMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Masjid = new Intent(MainActivity.this, DetailActivity.class);
                Masjid.putExtra("judul","Masjid");
                startActivity(Masjid);
            }
        });

    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean reqPermitGps(){
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                requestPermissions(new String[] {permission}, 101);
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        confirmQuit();
    }

    private void confirmQuit() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this,"Tekan kembali sekali lagi untuk Keluar",Toast.LENGTH_SHORT).show();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
