package com.example.drawforme;

import android.content.DialogInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;




import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class profileFrag extends Fragment implements View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    TextView appInfoBtn, howToUseBtn, tvProfileSection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        TextView email = v.findViewById(R.id.email_address);
        TextView logout_btn = v.findViewById(R.id.logout_btn);
        appInfoBtn = (TextView) v.findViewById(R.id.app_info_btn);
        howToUseBtn = (TextView) v.findViewById(R.id.how_to_use_btn);
        tvProfileSection = (TextView) v.findViewById(R.id.tv_profileSection);

        logout_btn.setOnClickListener(this);
        appInfoBtn.setOnClickListener(this);
        howToUseBtn.setOnClickListener(this);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
        email.setText(signInAccount.getEmail());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("68625167958-010el4r4e7frtqafddptojk2i76b6j64.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(v.getContext())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuth = FirebaseAuth.getInstance();

        return v;
    }

    // 클릭 리스너
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout_btn:
                // 로그아웃 다이얼로그 =========================================================
                AlertDialog.Builder alt_logout = new AlertDialog.Builder(view.getContext());
                alt_logout.setMessage("로그아웃 하시겠습니까?").setCancelable(false)
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                signOut();  // <<<<<<<<<<<<<<<< 로그아웃 여기!!
                                Toast.makeText(view.getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = alt_logout.create();
                alert.setTitle("로그아웃");
                alert.show();
                // 로그아웃 다이얼로그 =========================================================

                break;

            case R.id.app_info_btn:
                tvProfileSection.setText(R.string.app_info_str);

                break;
            case R.id.how_to_use_btn:
                tvProfileSection.setText(R.string.how_to_use_str);

                break;

            default:
                break;
        }
    }

    // 로그아웃 함수
    public void signOut() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                mAuth.signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                getActivity().setResult(1);
                            } else {
                                getActivity().setResult(0);
                            }
                            getActivity().finish();
                        }
                    });
                }
            }
            @Override
            public void onConnectionSuspended(int i) {
                getActivity().setResult(-1);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient!= null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
}
