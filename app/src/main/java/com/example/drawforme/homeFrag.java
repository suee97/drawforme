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
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Home화면 + pager연결
 */

public class homeFrag extends Fragment {
    ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        TextView nameTv = v.findViewById(R.id.name);
        pager = v.findViewById(R.id.pager_s);
        pager.setOffscreenPageLimit(2);

        PagerAdapter PA = new PagerAdapter(getFragmentManager());
        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        Fragment3 fragment3 = new Fragment3();
        PA.addItem(fragment1);
        PA.addItem(fragment2);
        PA.addItem(fragment3);
        pager.setAdapter(PA);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (signInAccount != null) {
            nameTv.setText(signInAccount.getDisplayName() + "님 환영합니다.");
        } else {
            nameTv.setText("invalid user");
        }
        return v;
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
        return null;
    }
}