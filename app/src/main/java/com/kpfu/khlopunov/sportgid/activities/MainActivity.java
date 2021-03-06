package com.kpfu.khlopunov.sportgid.activities;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.SectionsPagerAdapter;
import com.kpfu.khlopunov.sportgid.adapters.TabPagerAdapter;
import com.kpfu.khlopunov.sportgid.custom.TabViewWrapper;
import com.kpfu.khlopunov.sportgid.fragments.HomeFragment;
import com.kpfu.khlopunov.sportgid.fragments.NotifyFragment;
import com.kpfu.khlopunov.sportgid.fragments.OnBackPressedListener;
import com.kpfu.khlopunov.sportgid.fragments.ProfileFragment;
import com.kpfu.khlopunov.sportgid.fragments.SettingsFragment;
import com.kpfu.khlopunov.sportgid.providers.SharedPreferencesProvider;
import com.vk.sdk.util.VKUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NotifyFragment{

//    @BindView(R.id.tab_pager)
//    ViewPager mTabPager;
//    @BindView(R.id.tabs)
//    LinearLayout mTabLayout;
//    private TabPagerAdapter mTabPagerAdapter;
//    private TabViewWrapper mTabViewWrapper;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mTabPager.findViewById(R.id.tab_pager);
//        mTabLayout.findViewById(R.id.tabs);

        ButterKnife.bind(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mSectionsPagerAdapter.setNotifyFragment(this);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

//        mTabViewWrapper = new TabViewWrapper(mTabLayout);
//        mTabViewWrapper.setSelectedTab(0);
//        mTabViewWrapper.setSelectColor(R.color.button_pressed);
//        mTabViewWrapper.setTabListenerClick(this);
//        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
//        List<Fragment> tabFragment = new ArrayList<>();
//        HomeFragment homeFragment = HomeFragment.newInstance(this);
//        tabFragment.add(homeFragment);
//        ProfileFragment profileFragment = ProfileFragment.getInstance(MainActivity.this);
//        profileFragment.setNotifyFragment(this);
////        profileFragment.setUpdateData(() -> homeFragment.notifyDataSetChanged());
//        tabFragment.add(profileFragment);
//        SettingsFragment settingsFragment = SettingsFragment.getInstance();
//        settingsFragment.setNotifyFragment(this);
//        tabFragment.add(settingsFragment);
//        mTabPagerAdapter.setFragmentList(tabFragment);
//        mTabPager.setAdapter(mTabPagerAdapter);

        if (SharedPreferencesProvider.getInstance(this).getCity() == null || SharedPreferencesProvider.getInstance(this).getCity() == "")
            SharedPreferencesProvider.getInstance(this).saveCity("Казань");


//        mTabPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                mTabViewWrapper.setSelectedTab(position);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mTabViewWrapper.setSelectedTab(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

//    @Override
//    public void onTabClick(int position, View tab) {
//        mTabPager.setCurrentItem(position, true);
//    }
//
//    @Override
//    public void notifyData() {
//        mTabPagerAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                backPressedListener.onBackPressed();
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void notifyData() {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }
}

