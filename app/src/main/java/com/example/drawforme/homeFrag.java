package com.example.drawforme;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

public class homeFrag extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.home_fragment,container,false);

        TextView howTv = v.findViewById(R.id.check_how_to_tv);
        TextView nameTv = v.findViewById(R.id.name);
        howTv.setOnClickListener(this);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (signInAccount != null) {
            nameTv.setText(signInAccount.getDisplayName() + "님 환영합니다.");
        }else {
            nameTv.setText("invalid user");
        }
        return v;
    }

    // 클릭 리스너
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.check_how_to_tv:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("사용법");
                builder.setMessage(R.string.dlg_1);
                builder.show();
                break;
        }
    }
}