package com.example.drawforme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.Value;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private int FROM_ALBUM = 10;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private ImageView postIv;
    private FirebaseUser user;

    String strTitle, strDesc, uuid, strAuth;
    TextView tvTitle, tvDesc, tvNewComment, deleteBtn;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        postIv = (ImageView) findViewById(R.id.post_iv);
        tvTitle = (TextView) findViewById(R.id.get_title_from_adapter);
        tvDesc = (TextView) findViewById(R.id.get_desc_from_adapter);
        tvNewComment = (TextView) findViewById(R.id.new_comment_tv);
        deleteBtn = (TextView) findViewById(R.id.delete_post_btn);
        deleteBtn.setOnClickListener(this);

        // ?????? ?????????????????? ????????? ????????????
        uuid = getIntent().getStringExtra("uuid_");
        strTitle = getIntent().getStringExtra("title_");
        strDesc = getIntent().getStringExtra("desc_");
        strAuth = getIntent().getStringExtra("author_");

        // ??????, ?????? ????????? ??????
        tvTitle.setText(strTitle);
        tvDesc.setText(strDesc);
        tvDesc.setMovementMethod(new ScrollingMovementMethod());

        // ?????? ??????????????? ??????
        tvNewComment.setOnClickListener(this);

        // ????????? ????????? ????????? ???????????? disabled
        if(user.getDisplayName().equals(strAuth) != true) {
            deleteBtn.setBackgroundResource(R.drawable.border10);
            deleteBtn.setEnabled(false);
        }

        // ????????? ????????? ????????????
        StorageReference storageRef = storage
                .getReferenceFromUrl("gs://drawforme-58157.appspot.com/");
        storageRef.child("post_images/" + uuid + "_0000").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // ????????? ?????? ?????????
                tvNewComment.setVisibility(View.INVISIBLE);
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(postIv);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("????????? ????????????. ??????????????????!");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case 10: {
                //???????????? ????????????
                if (data.getData() != null) {
                    try {
                        photoURI = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                        postIv.setImageBitmap(bitmap);
                        String filename = uuid + "_0000";
                        StorageReference storageRef = storage
                                .getReferenceFromUrl("gs://drawforme-58157.appspot.com/")
                                .child("post_images/" + filename);
                        UploadTask uploadTask;
                        Uri file = null;
                        file = photoURI;
                        uploadTask = storageRef.putFile(file); // storage??? ???????????? ??????

                        Toast.makeText(getApplicationContext(), "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                        tvNewComment.setVisibility(View.INVISIBLE);

                        // ????????? ????????????
                        updateDB(strTitle, strDesc, strAuth, uuid, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    public void selectAlbum() {
        //???????????? ????????? ?????????
        //?????? ??????
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_ALBUM);
    }

    // DB???????????? ??????(REAL TIME DB)
    public void updateDB(String strTitle, String strDesc, String strAuth, String uuid, Boolean isExist) {
        PostModel PM = new PostModel(strTitle, strDesc, strAuth, uuid, isExist);
        Map<String, Object> postValues = PM.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/posts/" + uuid, postValues);
        databaseReference.updateChildren(childUpdate);
    }

    // ???????????????
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // ?????? ??????(?????????, ?????? ?????????)
            case R.id.delete_post_btn:
                if (user.getDisplayName().equals(strAuth) != true) {
                    makeToast("???????????? ????????? ????????????.");
                } else {
                    AlertDialog.Builder builder_delete = new AlertDialog.Builder(v.getContext());
                    builder_delete.setTitle("??????")
                            .setMessage("?????????????????????????")
                            .setPositiveButton("???", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Query deleteQuery = databaseReference.child("posts/" + uuid);
                                    deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                                childSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            makeToast("????????? ??????????????????.");
                                        }
                                    });

                                    // ??? ????????? ??????
                                    Intent goHomeIntent = new Intent(v.getContext(), HomeActivity.class);
                                    startActivity(goHomeIntent);
                                    makeToast("?????????????????????.");
                                }
                            })
                            .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // ??????????????? ?????????
                                }
                            });

                    AlertDialog alert_delete = builder_delete.create();
                    alert_delete.show();
                }
                break;

            // ???????????? ??????
            case R.id.new_comment_tv:
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("????????? ?????????")
                        .setMessage("????????? ??????????????? ????????????\n???????????? ???????????????.")
                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selectAlbum();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    // ????????? ????????? + ????????????
    public void makeToast(String msg) {
        Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

