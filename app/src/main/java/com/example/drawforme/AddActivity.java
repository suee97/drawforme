package com.example.drawforme;

import static android.os.Build.ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddActivity extends AppCompatActivity{

    EditText editTitle, editDesc;
    TextView saveBtn;
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        editTitle = (EditText) findViewById(R.id.get_title_et);
        editDesc = (EditText) findViewById(R.id.get_desc_et);
        saveBtn = (TextView) findViewById(R.id.save_title_desc_btn);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTitle.getText().toString().isEmpty() || editDesc.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "질문을 마저 채워주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    // 데이터 저장 부분 ==================================================================== //
                    PostModel PM = new PostModel();
                    PM.setIsExist(false); // 처음엔 그림이 없다고 저장
                    PM.setTitle(editTitle.getText().toString());
                    PM.setDesc(editDesc.getText().toString());
                    PM.setAuthor(mAuth.getCurrentUser().getDisplayName());

                    Long tmp_0 = System.currentTimeMillis(); // 현재시간으로 id생성
                    String time_uuid = tmp_0.toString();
                    PM.setUuid(time_uuid);

                    database.getReference().child("posts").child(time_uuid).setValue(PM); // 데이터 추가
                    editTitle.setText("");
                    editDesc.setText("");
                    finish();
                    Toast.makeText(getApplicationContext(), "저장이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    // ===================================================================== 데이터 저장 부분 //
                }
            }
        });
    }
}