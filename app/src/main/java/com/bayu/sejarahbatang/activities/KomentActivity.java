package com.bayu.sejarahbatang.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.adapter.KomentarAdapter;
import com.bayu.sejarahbatang.api.Client;
import com.bayu.sejarahbatang.api.Service;
import com.bayu.sejarahbatang.maps.MapActivity;
import com.bayu.sejarahbatang.objek.Koment;
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

public class KomentActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Koment> mArrayList;
    private KomentarAdapter mAdapter;
    TextView lihat_komen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_komentar);

        lihat_komen = findViewById(R.id.tv_lihat_semua);
        lihat_komen.setVisibility(View.GONE);

        mRecyclerView = findViewById(R.id.komentar_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        String id = getIntent().getStringExtra("id");
        loadJson(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent kembali = new Intent(KomentActivity.this, MapActivity.class);
        startActivity(kembali);
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

//                Toast.makeText(KomentActivity.this, "respon error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(KomentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}
