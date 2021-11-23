package com.example.drawforme;

import static android.os.Build.ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button saveBtn;
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        /* 새로운 글 등록 화면 */

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        editTitle = (EditText) findViewById(R.id.get_title_et);
        editDesc = (EditText) findViewById(R.id.get_desc_et);
        saveBtn = (Button) findViewById(R.id.save_title_desc_btn);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTitle.getText().toString().isEmpty() || editDesc.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "질문을 마저 채워주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    PostModel PM = new PostModel();
                    // 처음엔 그림이 없다고 저장
                    PM.setIsExist(false);

                    PM.setTitle(editTitle.getText().toString());
                    PM.setDesc(editDesc.getText().toString());
                    PM.setAuthor(mAuth.getCurrentUser().getDisplayName());

                    // 랜덤id
                    Long tmp_0 = System.currentTimeMillis();
                    String time_uuid = tmp_0.toString();
//                    String randomUUid = (UUID.randomUUID().toString().replaceAll("-",""));
                    PM.setUuid(time_uuid);

                    database.getReference().child("posts").child(time_uuid).setValue(PM); // 데이터 추가
                    editTitle.setText("");
                    editDesc.setText("");
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "저장이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}