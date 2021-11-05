package com.example.drawforme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    TextView nameTv, emailTv, howToUseDialog;
//    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        emailTv = (TextView) findViewById(R.id.email_address);
//        logoutBtn = (Button) findViewById(R.id.logoutButton);
        howToUseDialog = (TextView) findViewById(R.id.check_how_to_tv);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null) {
            emailTv.setText(signInAccount.getDisplayName() + "님 환영합니다.");
        }


//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "로그아웃합니다.", Toast.LENGTH_LONG).show();
//            }
//        });

        howToUseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlyMsgDlg(R.string.dlg_1);
            }
        });
    }

    public void onlyMsgDlg(int msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사용법");
        builder.setMessage(msg);
        builder.show();
    }
}