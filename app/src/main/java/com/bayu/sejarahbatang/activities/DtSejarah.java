package com.bayu.sejarahbatang.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bayu.sejarahbatang.R;

public class DtSejarah extends AppCompatActivity {

    TextView allSejarah;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dt_sejarah);

        allSejarah = findViewById(R.id.tv_all_sejarah);
        allSejarah.setText(getIntent().getStringExtra("sejarah"));
    }
}
