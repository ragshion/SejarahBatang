package com.bayu.sejarahbatang.mapskategori;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.activities.DtActivity;
import com.bayu.sejarahbatang.api.Client;
import com.bayu.sejarahbatang.api.Service;
import com.bayu.sejarahbatang.gps.GPSTracker;
import com.bayu.sejarahbatang.maps.MapWrapperLayout;
import com.bayu.sejarahbatang.maps.OnInfoWindowElemTouchListener;
import com.bayu.sejarahbatang.objek.Sejarah;
import com.dd.processbutton.FlatButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapKategoriActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private FlatButton infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    Double lati,lngi;
    private List<Sejarah> sejarahs= new ArrayList<>();
    int i;

    private GoogleMap mMap;

    MapFragment mapFragment;
    MapWrapperLayout mapWrapperLayout;

    Toolbar toolbar;
    String kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        kategori = getIntent().getStringExtra("kategori");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(kategori);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        mapFragment.getMapAsync(this);


    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        GPSTracker gps = new GPSTracker(MapKategoriActivity.this);

        BitmapDescriptor icon;

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.location);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 130, false);
        icon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        map.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude(),gps.getLongitude())).title("Lokasi Saya").icon(icon));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),gps.getLongitude()), 16.0f));

        mMap = map;
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));


//        tampilDataLokasi(gps.getLatitude(),gps.getLongitude());


        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window, null);
        this.infoTitle = infoWindow.findViewById(R.id.title);
        this.infoSnippet = infoWindow.findViewById(R.id.snippet);
        this.infoButton = infoWindow.findViewById(R.id.btnDetail);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.color.md_blue_grey_600),
                getResources().getDrawable(R.color.md_blue_grey_600))
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                Intent detail = new Intent(MapKategoriActivity.this, DtActivity.class);
                detail.putExtra("id",marker.getSnippet());
                detail.putExtra("judul", marker.getTitle());
                startActivity(detail);

            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);


        map.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoSnippet.setText(marker.getSnippet());
                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        tampilDataLokasi(gps.getLatitude(),gps.getLongitude(), kategori);

    }

    private void tampilDataLokasi(Double lat, Double lng, final String kategori){
        lati = lat;
        lngi = lng;

        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.getWhere(String.valueOf(lat),String.valueOf(lng), kategori);

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                String opdstring = response.body().toString();

                Type listType = new TypeToken<List<Sejarah>>() {}.getType();
                sejarahs = getTeamListFromJson(opdstring, listType);

                for (i=0; i<sejarahs.size(); i++){
                    Double latitude = Double.parseDouble(sejarahs.get(i).getLatitude());
                    Double longitude = Double.parseDouble(sejarahs.get(i).getLongitude());
                    LatLng location = new LatLng(latitude, longitude);

                    BitmapDescriptor icon;

                    BitmapDrawable bitmapdraw;

                    if(kategori.equalsIgnoreCase("bangunan")){
                        bitmapdraw =(BitmapDrawable)getResources().getDrawable(R.drawable.bank);
                    }else if(kategori.equalsIgnoreCase("masjid")){
                        bitmapdraw =(BitmapDrawable)getResources().getDrawable(R.drawable.mosque);
                    }else if(kategori.equalsIgnoreCase("makam")){
                        bitmapdraw =(BitmapDrawable)getResources().getDrawable(R.drawable.cementery);
                    }else{
                        bitmapdraw =(BitmapDrawable)getResources().getDrawable(R.drawable.cave);
                    }

                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 90, 120, false);
                    icon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                    mMap.addMarker(new MarkerOptions().position(location).title(sejarahs.get(i).getNama()).snippet(sejarahs.get(i).getIdSejarah()).icon(icon));

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("ERROR FAILUR",t.getMessage());
                Toast.makeText(MapKategoriActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static <T> List<T> getTeamListFromJson(String jsonString, Type type) {
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

    static String before(String value, String a) {
        // Return substring containing all characters before a string.
        int posA = value.indexOf(a);
        if (posA == -1) {
            return "";
        }
        return value.substring(0, posA);
    }

    static String after(String value, String a) {
        // Returns a substring containing all characters after a string.
        int posA = value.lastIndexOf(a);
        if (posA == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= value.length()) {
            return "";
        }
        return value.substring(adjustedPosA);
    }


}



