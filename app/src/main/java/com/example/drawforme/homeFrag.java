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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class homeFrag extends Fragment implements View.OnClickListener{
    ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.home_fragment,container,false);

        TextView nameTv = v.findViewById(R.id.name);
        pager = v.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        PagerAdapter PA = new PagerAdapter(getFragmentManager());
        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        PA.addItem(fragment1);
        PA.addItem(fragment2);
        pager.setAdapter(PA);

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
        // 클릭 리스너
    }
}


@SuppressWarnings("deprecation")
class PagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> items = new ArrayList<Fragment>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment item) {
        items.add(item);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "페이지" + position;
    }
}