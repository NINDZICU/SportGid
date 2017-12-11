package com.kpfu.khlopunov.sportgid.activities;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.kpfu.khlopunov.sportgid.R;
import com.kpfu.khlopunov.sportgid.adapters.TabPagerAdapter;
import com.kpfu.khlopunov.sportgid.custom.TabViewWrapper;
import com.kpfu.khlopunov.sportgid.fragments.HomeFragment;
import com.kpfu.khlopunov.sportgid.fragments.ProfileFragment;
import com.kpfu.khlopunov.sportgid.fragments.SettingsFragment;
import com.vk.sdk.util.VKUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabViewWrapper.TabListener {

    @BindView(R.id.tab_pager)
    ViewPager mTabPager;
    @BindView(R.id.tabs)
    LinearLayout mTabLayout;
    private TabPagerAdapter mTabPagerAdapter;
    private TabViewWrapper mTabViewWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mTabPager.findViewById(R.id.tab_pager);
//        mTabLayout.findViewById(R.id.tabs);

        ButterKnife.bind(this);
        mTabViewWrapper = new TabViewWrapper(mTabLayout);
        mTabViewWrapper.setSelectedTab(0);
        mTabViewWrapper.setSelectColor(R.color.button_pressed);
        mTabViewWrapper.setTabListenerClick(this);
        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        List<Fragment> tabFragment = new ArrayList<>();
        HomeFragment homeFragment = HomeFragment.newInstance();
        tabFragment.add(homeFragment);
        ProfileFragment profileFragment = ProfileFragment.getInstance();
//        profileFragment.setUpdateData(() -> homeFragment.notifyDataSetChanged());
        tabFragment.add(profileFragment);
        tabFragment.add(SettingsFragment.getInstance());
        mTabPagerAdapter.setFragmentList(tabFragment);
        mTabPager.setAdapter(mTabPagerAdapter);


        mTabPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabViewWrapper.setSelectedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
    }

    @Override
    public void onTabClick(int position, View tab) {
        mTabPager.setCurrentItem(position, true);
    }
}

