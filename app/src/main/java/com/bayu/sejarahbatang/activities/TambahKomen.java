package com.bayu.sejarahbatang.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bayu.sejarahbatang.R;
import com.bayu.sejarahbatang.api.Client;
import com.bayu.sejarahbatang.api.Service;
import com.dd.processbutton.FlatButton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKomen extends AppCompatActivity {
    TextView tv_nama, tv_komentar, tv_alamat;
    FlatButton btnSimpan;
    MaterialDialog materialDialog;

    String id;

    String ids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_komentar);

        id = getIntent().getStringExtra("id");

        tv_nama = findViewById(R.id.tv_nama);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_komentar = findViewById(R.id.tv_komentar);
        btnSimpan = findViewById(R.id.btnUpload);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog.Builder(TambahKomen.this)
                        .content("Sedang Menambahkan...")
                        .progress(true, 0)
                        .cancelable(false)
                        .progressIndeterminateStyle(true)
                        .show();
                tambahKomentar(id,tv_alamat.getText().toString(),tv_nama.getText().toString(), tv_komentar.getText().toString());
            }
        });

    }

    void tambahKomentar(String id, String nama, String alamat, String komentar){
        ids = id;
        Service serviceApi = Client.getClient();
        Call<ResponseBody> updateSiap = serviceApi.addKoment(id,alamat,nama,komentar);
        updateSiap.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(TambahKomen.this, "Komentar disimpan, Komentar akan ditampilkan apabila sudah mendapatkan persetujuan admin", Toast.LENGTH_LONG).show();
                materialDialog.dismiss();
                Intent kembali = new Intent(TambahKomen.this, DtActivity.class);
                kembali.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                kembali.putExtra("id",ids);
                startActivity(kembali);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahKomen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.v("UpdateStatusSiap",t.getMessage());
                Intent kembali = new Intent(TambahKomen.this, DtActivity.class);
                kembali.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                kembali.putExtra("id",ids);
                startActivity(kembali);
                finish();
            }
        });
    }

}
