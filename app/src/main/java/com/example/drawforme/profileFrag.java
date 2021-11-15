package com.example.drawforme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nullable;

public class profileFrag extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profile_fragment,container,false);
        TextView email = v.findViewById(R.id.email_address);
        TextView logout_btn = v.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(this);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
        email.setText(signInAccount.getEmail());

        return v;
    }

    // 클릭 리스너
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.logout_btn:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(requireContext(), "로그아웃합니다.", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
