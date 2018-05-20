package com.kpfu.khlopunov.sportgid.adapters;

/**
 * Created by hlopu on 20.05.2018.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kpfu.khlopunov.sportgid.fragments.HomeFragment;
import com.kpfu.khlopunov.sportgid.fragments.ProfileFragment;
import com.kpfu.khlopunov.sportgid.fragments.SettingsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int COUNT_ITEMS = 3;
    private String tabTitles[] = new String[]{"Home", "Профиль", "Настройки"};
    private Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeFragment.newInstance(context);
            case 1:
                return ProfileFragment.getInstance(context);
            case 2:
                return SettingsFragment.getInstance();
            default:
                return HomeFragment.newInstance(context);
        }
    }

    @Override
    public int getCount() {
        return COUNT_ITEMS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}