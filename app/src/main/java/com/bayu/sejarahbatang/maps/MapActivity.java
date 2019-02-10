package com.bayu.sejarahbatang.maps;


/*import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;*/

/*premkumarnew80@gmail.com*/

import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.activities.DataActivity;
import com.bayu.sejarahbatang.activities.DetailActivity;
import com.bayu.sejarahbatang.activities.DtActivity;
import com.bayu.sejarahbatang.api.Client;
import com.bayu.sejarahbatang.api.Service;
import com.bayu.sejarahbatang.gps.GPSTracker;
import com.bayu.sejarahbatang.mapskategori.MapKategoriActivity;
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
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private TextView infoSnippet;
    private FlatButton infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    Double lati,lngi;
    private List<Sejarah> sejarahs= new ArrayList<>();
    int i;

    private GoogleMap mMap;

    Drawer navDrawerLeft;
    AccountHeader headerNavLeft;

    private boolean doubleBackToExitPressedOnce = false;

    MapFragment mapFragment;
    MapWrapperLayout mapWrapperLayout;

    Toolbar toolbar;

    PrimaryDrawerItem drawerGoa,drawerBangunan,drawerMakam,drawerMasjid,drawerShare, drawerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reqPermitGPS();
        reqPermitPhone();

        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        mapFragment.getMapAsync(this);

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge

        drawerMap = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withName("Peta")
                .withIcon(R.drawable.map2);

        drawerBangunan = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("Bangunan")
                .withIcon(R.drawable.bank);

        drawerMasjid = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Masjid")
                .withIcon(R.drawable.mosque);

        drawerMakam = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Makam")
                .withIcon(R.drawable.cementery);

        drawerGoa = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName("Goa")
                .withIcon(R.drawable.cave);

        drawerShare = new PrimaryDrawerItem()
                .withIdentifier(6)
                .withName("Share")
                .withIcon(R.drawable.share);

        navDrawerLeft = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerNavLeft)
                .addDrawerItems(
                        drawerMap,
                        new DividerDrawerItem(),
                        drawerBangunan,
                        drawerMasjid,
                        drawerMakam,
                        drawerGoa,
                        new DividerDrawerItem(),
                        drawerShare
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 2:
                                Intent bangunan = new Intent(MapActivity.this, MapKate.class);
                                bangunan.putExtra("kategori","bangunan");
                                startActivity(bangunan);
                                navDrawerLeft.setSelection(drawerMap);
                                break;

                            case 3:
                                Intent masjid = new Intent(MapActivity.this, MapKate.class);
                                masjid.putExtra("kategori","masjid");
                                startActivity(masjid);
                                navDrawerLeft.setSelection(drawerMap);
                                break;

                            case 4:
                                Intent makam = new Intent(MapActivity.this, MapKate.class);
                                makam.putExtra("kategori","makam");
                                startActivity(makam);
                                navDrawerLeft.setSelection(drawerMap);
                                break;

                            case 5:
                                Intent goa = new Intent(MapActivity.this, MapKate.class);
                                goa.putExtra("kategori","goa");
                                startActivity(goa);
                                navDrawerLeft.setSelection(drawerMap);
                                break;

                            case 7:
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String isiShare = "Aplikasi Sejarah Batang \n\nAyo Unduh di PlayStore";
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, isiShare);
                                startActivity(Intent.createChooser(sharingIntent, "Bagikan Via :"));
                                navDrawerLeft.setSelection(0);
                                break;

                        }
                        return false;
                    }
                })
                .build();

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
        Toast.makeText(this,"Tekan Kembali Sekali Lagi Untuk Keluar...",Toast.LENGTH_SHORT).show();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean reqPermitGPS(){
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                requestPermissions(new String[] {permission}, 101);
            }
        }
        return true;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        GPSTracker gps = new GPSTracker(MapActivity.this);

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
                Intent detail = new Intent(MapActivity.this, DtActivity.class);
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

        tampilDataLokasi(gps.getLatitude(),gps.getLongitude());

    }

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

    private void tampilDataLokasi(Double lat, Double lng){
        lati = lat;
        lngi = lng;

        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.getData(String.valueOf(lat),String.valueOf(lng));

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

                    mMap.addMarker(new MarkerOptions().position(location).title(sejarahs.get(i).getNama()).snippet(sejarahs.get(i).getIdSejarah()));

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("ERROR FAILUR",t.getMessage());
                Toast.makeText(MapActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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



