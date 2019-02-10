package com.bayu.sejarahbatang.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.adapter.KomentarAdapter;
import com.bayu.sejarahbatang.api.Client;
import com.bayu.sejarahbatang.api.Service;
import com.bayu.sejarahbatang.gps.GPSTracker;
import com.bayu.sejarahbatang.objek.Carousel;
import com.bayu.sejarahbatang.objek.Koment;
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.dd.processbutton.FlatButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DtActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private ArrayList<Koment> mArrayList;
    private KomentarAdapter mAdapter;

    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fabKomen;
    TextView tv_lihat_semua;

    TextView tv_dt_nama, tv_dt_tahun,tv_dt_alamat, tv_dt_kecamatan,  tv_dt_jam,  tv_dt_sumber, tv_bdt, tv_readmore,tv_dt_telepon;

    String ids;

    private List<Carousel> carousels= new ArrayList<>();

    SliderLayout sliderLayout;

    String dtSejarah, allSejarah;

    ImageView imgTelepon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        reqPermitPhone();

        toolbar = findViewById(R.id.maintoolbar);
        toolbar.setTitle(getIntent().getStringExtra("judul"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgTelepon = findViewById(R.id.imgTelepon);

        tv_dt_nama = findViewById(R.id.tv_dt_nama);
        tv_dt_alamat = findViewById(R.id.tv_dt_alamat);
        tv_dt_jam = findViewById(R.id.tv_dt_jam);
        tv_dt_kecamatan = findViewById(R.id.tv_dt_kecamatan);
        tv_dt_sumber = findViewById(R.id.tv_dt_sumber);
        tv_dt_tahun = findViewById(R.id.tv_dt_tahun);
        tv_bdt = findViewById(R.id.tv_bdt_nama);
        tv_readmore = findViewById(R.id.tv_readmore);
        tv_dt_telepon = findViewById(R.id.tv_dt_telepon);

        coordinatorLayout = findViewById(R.id.coordinator);
        collapsingToolbarLayout =  findViewById(R.id.maincollapsing);
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("judul"));
        tv_bdt.setText(getIntent().getStringExtra("judul"));

        fabKomen = findViewById(R.id.fabKomen);

        tv_lihat_semua = findViewById(R.id.tv_lihat_semua);

        fabKomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent komen = new Intent(DtActivity.this, TambahKomen.class);
                komen.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(komen);
            }
        });

        tv_lihat_semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lihatsemua = new Intent(DtActivity.this, KomentActivity.class);
                lihatsemua.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(lihatsemua);
            }
        });

        tv_readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent readmore = new Intent(DtActivity.this, DtSejarah.class);
                readmore.putExtra("sejarah", allSejarah);
                startActivity(readmore);
            }
        });

        imgTelepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomor = "tel:"+tv_dt_telepon.getText().toString().trim();
                Intent manggilIntent = new Intent(Intent.ACTION_CALL);
                manggilIntent.setData(Uri.parse(nomor));
                if(ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                v.getContext().startActivity(manggilIntent);
            }
        });

        mRecyclerView = findViewById(R.id.komentar_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        String id = getIntent().getStringExtra("id");
        loadJson(id);

        GPSTracker gps = new GPSTracker(DtActivity.this);

        loadDetail(gps.getLatitude(), gps.getLongitude(), getIntent().getStringExtra("id"));

        initimageSlider(id);

    }


    void loadDetail(Double lati, Double lngi, String id){
        Service serviceAPI = Client.getClient();
        serviceAPI.getDetail(lati,lngi,id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    JSONObject jsonRESULTS = new JSONObject(response.body().string());

                    tv_dt_nama.setText(jsonRESULTS.getJSONObject("data").getString("nama"));
                    tv_dt_alamat.setText(jsonRESULTS.getJSONObject("data").getString("alamat"));
                    tv_dt_jam.setText(jsonRESULTS.getJSONObject("data").getString("jam_buka")+" - "+jsonRESULTS.getJSONObject("data").getString("jam_tutup"));

                    tv_dt_kecamatan.setText(jsonRESULTS.getJSONObject("data").getString("kecamatan"));
                    tv_dt_sumber.setText(jsonRESULTS.getJSONObject("data").getString("sumber"));

                    dtSejarah = jsonRESULTS.getJSONObject("data").getString("sejarah");
                    dtSejarah = dtSejarah.substring(0,120)+"...";

                    allSejarah = jsonRESULTS.getJSONObject("data").getString("sejarah");

                    tv_dt_telepon.setText(jsonRESULTS.getJSONObject("data").getString("no_pengelola"));

                    tv_dt_tahun.setText(dtSejarah);



                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("ErrorGetData",e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String error = "Anda Tidak Terhubung Ke Internet, Silahkan Periksa Koneksi Anda";
                Toast.makeText(DtActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initimageSlider(String id){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadFile = serviceAPI.getCarousel(id);
        loadFile.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String faskesString = response.body().toString();

                Type listType = new TypeToken<List<Carousel>>() {}.getType();
                carousels = getTeamListFromJson(faskesString, listType);

                sliderLayout = findViewById(R.id.detailSlider);

                HashMap<String,String> url_maps = new HashMap<String, String>();

                for(int i=0; i<carousels.size(); i++){
                    String path = getString(R.string.pathFoto)+carousels.get(i).getFileName();
                    url_maps.put(carousels.get(i).getIdFiles(),path);
                }

                for(String name : url_maps.keySet()){
//                    DefaultSliderView textSliderView = new DefaultSliderView(DetailImageTindak.this);
                    // initialize a SliderLayout
                    DefaultSliderView textSliderView = new DefaultSliderView(DtActivity.this);
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(DtActivity.this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);

                    sliderLayout.addSlider(textSliderView);
                }

                // you can change animasi, time page and anythink.. read more on github
                sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                sliderLayout.setCustomAnimation(new DescriptionAnimation());
//                sliderLayout.setDuration(5000);
//                sliderLayout.startAutoCycle();
                sliderLayout.stopAutoCycle();

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void loadJson(String id){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadNomor = serviceAPI.getKoment(id);
        loadNomor.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String faskesString = response.body().toString();

                Type listType = new TypeToken<ArrayList<Koment>>() {}.getType();
                mArrayList = getTeamListFromJson(faskesString, listType);

                mAdapter = new KomentarAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
//                Toast.makeText(DtActivity.this, "respon error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(DtActivity.this, "respon error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // OVERRIDE FOR IMAGE SLIDER


    @TargetApi(Build.VERSION_CODES.M)
    public boolean reqPermitPhone(){
        String permission = Manifest.permission.CALL_PHONE;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                requestPermissions(new String[] {permission}, 101);
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
//            Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//            Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    // END OF OVERRIDE FOR IMAGE SLIDER


    public static <T> ArrayList<T> getTeamListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }
}
