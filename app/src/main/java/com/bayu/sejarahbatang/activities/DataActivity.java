package com.bayu.sejarahbatang.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.adapter.DataAdapter;
import com.bayu.sejarahbatang.api.Client;
import com.bayu.sejarahbatang.api.Service;
import com.bayu.sejarahbatang.gps.GPSTracker;
import com.bayu.sejarahbatang.objek.Sejarah;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Sejarah> mArrayList;
    private DataAdapter mAdapter;
    private MenuItem search;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String kategori = getIntent().getStringExtra("kategori");
        this.setTitle(kategori);

        mRecyclerView = findViewById(R.id.data_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        GPSTracker gps = new GPSTracker(this);
        loadJSON(gps.getLatitude(), gps.getLongitude(),kategori);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadJSON(Double lat, Double lng, String kategori){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadSearch = serviceAPI.getWhere(String.valueOf(lat),String.valueOf(lng),kategori);
        loadSearch.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String faskesString = response.body().toString();

                Type listType = new TypeToken<ArrayList<Sejarah>>() {}.getType();
                mArrayList = getTeamListFromJson(faskesString, listType);

                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new DataAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem cari = menu.findItem(R.id.btnSearch);
        cari.setVisible(false);

        search = menu.findItem(R.id.search);
        searchView  = (SearchView) search.getActionView();
        search(searchView);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) {
                    mAdapter.getFilter().filter(newText);

                }
                return true;
            }
        });
    }

}
