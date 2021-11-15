package com.example.drawforme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class PostActivity extends AppCompatActivity {
    String strTitle, strDesc;
    TextView tvTitle, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        tvTitle = (TextView) findViewById(R.id.get_title_from_adapter);
        tvDesc = (TextView) findViewById(R.id.get_desc_from_adapter);

        strTitle = getIntent().getStringExtra("title_");
        strDesc = getIntent().getStringExtra("desc_");

        tvTitle.setText(strTitle);
        tvDesc.setText(strDesc);
    }
}