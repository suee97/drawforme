package com.example.drawforme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Map;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {

    private int FROM_ALBUM = 10;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ImageView postIv;

    String strTitle, strDesc, uuid, strAuth;
    TextView tvTitle, tvDesc, tvNewComment;
    Button imgUploadBtn;
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
        imgUploadBtn = (Button) findViewById(R.id.img_upload_btn);

        // 이전 액티비티에서 데이터 받아오기
        uuid = getIntent().getStringExtra("uuid_");
        strTitle = getIntent().getStringExtra("title_");
        strDesc = getIntent().getStringExtra("desc_");
        strAuth = getIntent().getStringExtra("author_");

        // 제목, 내용 보이게 하기
        tvTitle.setText(strTitle);
        tvDesc.setText(strDesc + "\nuuid = " + uuid);

        // 추가버튼 클릭 시
        tvNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("이미지 업로드")
                        .setMessage("그림 업로드하기\n아래 추가버튼을 눌러주세요")
                        .setPositiveButton("추가하기", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selectAlbum();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

//                Dialog imgDialog = new Dialog(PostActivity.this);
//                imgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                imgDialog.setContentView(R.layout.image_upload_dialog);
//                imgDialog.show();
//                selectAlbum();
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
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(postIv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("그림이 없습니다. 추가해주세요!");
            }
        });


        // Value Event 리스너
        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    PostModel pm = childSnapshot.getValue(PostModel.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            Toast.makeText(getApplicationContext(), "이미지 선택 안함", Toast.LENGTH_SHORT).show();
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


//                        HashMap hashMap = new HashMap();
//                        hashMap.put("isExist", true);
//                        String itemKey = database.getReference("posts").push().getKey();
//                        database.getReference().child("posts/"+itemKey).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
//                            @Override
//                            public void onSuccess(Object o) {
//                                Toast.makeText(getApplicationContext(), "업데이트 완료", Toast.LENGTH_SHORT).show();
//                            }
//                        });

//                        // 여기서 값수정
//                        String itemKey = database.getReference("posts").push().getKey();
//                        database.getReference().child("posts/" + itemKey).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                tmp_tmp.setText(itemKey);
//                                PostModel pm = snapshot.getValue(PostModel.class);
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                // 에러 발생시
//                            }
//                        });
                        updateDB(strTitle, strDesc, strAuth, uuid, true);

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

    public void updateDB(String strTitle, String strDesc, String strAuth, String uuid, Boolean isExist) {
        PostModel PM = new PostModel(strTitle, strDesc, strAuth, uuid, isExist);
        Map<String, Object> postValues = PM.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/posts/" + uuid, postValues);
        databaseReference.updateChildren(childUpdate);
    }

    public void makeToast(String msg) {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

