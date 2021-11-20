package com.example.drawforme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private int FROM_ALBUM = 10;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ImageView postIv;

    String strTitle, strDesc, uuid;
    TextView tvTitle, tvDesc, tvNewComment;
    TextView tmp_tmp;

    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        postIv = (ImageView) findViewById(R.id.post_iv);
        tvTitle = (TextView) findViewById(R.id.get_title_from_adapter);
        tvDesc = (TextView) findViewById(R.id.get_desc_from_adapter);
        tvNewComment = (TextView) findViewById(R.id.new_comment_tv);
        tmp_tmp = (TextView) findViewById(R.id.tmp_tmp);

        // 이전 액티비티에서 데이터 받아오기
        uuid = getIntent().getStringExtra("uuid_");
        strTitle = getIntent().getStringExtra("title_");
        strDesc = getIntent().getStringExtra("desc_");

        // 제목, 내용 보이게 하기
        tvTitle.setText(strTitle);
        tvDesc.setText(strDesc + "\nuuid = " + uuid);

        // 추가버튼 클릭 시 앨범열기 (처음으로 등록)
        tvNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAlbum(); // 앨범열기
            }
        });

        // 이미지 있으면 불러오기
        StorageReference storageRef = storage
                .getReferenceFromUrl("gs://drawforme-58157.appspot.com/");
        storageRef.child("post_images/" + uuid + "_0000").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // 이미지 로드 성공시
                tvNewComment.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "이미 등록된 그림이 있습니다.", Toast.LENGTH_LONG).show();
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(postIv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // 이미지 로드 실패시
                Toast.makeText(getApplicationContext(), "등록된 그림이 없습니다. 추가해주세요!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            Toast.makeText(getApplicationContext(), "취소!", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode){
            case 10 : {
                //앨범에서 가져오기
                if(data.getData() != null){
                    try{
                        photoURI = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        postIv.setImageBitmap(bitmap);
//                        final String cu = mAuth.getUid();
//                        String filename = cu + "_" + System.currentTimeMillis();
                        String filename = uuid + "_0000";
                        StorageReference storageRef = storage
                                .getReferenceFromUrl("gs://drawforme-58157.appspot.com/")
                                .child("post_images/" + filename);
                        UploadTask uploadTask;
                        Uri file = null;
                        file = photoURI;
                        uploadTask = storageRef.putFile(file); // storage에 저장하는 부분
                        Toast.makeText(getApplicationContext(), "등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        tvNewComment.setVisibility(View.INVISIBLE);


                        // 여기서 값수정
                        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                PostModel tmp = snapshot.getValue(PostModel.class);
                                String tmp2 = snapshot.getKey();
                                String tmp3 = tmp.getUuid();
                                tmp_tmp.setText(tmp + " // " + tmp2 + " // " + tmp3);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    public void selectAlbum(){
        //앨범에서 이미지 가져옴
        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM);
    }
}

