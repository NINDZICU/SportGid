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
import com.kpfu.khlopunov.sportgid.fragments.NotifyFragment;
import com.kpfu.khlopunov.sportgid.fragments.ProfileFragment;
import com.kpfu.khlopunov.sportgid.fragments.SettingsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter{
    private int COUNT_ITEMS = 3;
    private String tabTitles[] = new String[]{"Home", "Профиль", "Настройки"};
    private Context context;
    private NotifyFragment notifyFragment;

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
                ProfileFragment profileFragment = ProfileFragment.getInstance(context);
                profileFragment.setNotifyFragment(notifyFragment);
                return profileFragment;
            case 2:
                SettingsFragment settingsFragment = SettingsFragment.getInstance();
                settingsFragment.setNotifyFragment(notifyFragment);
                return settingsFragment;
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

    public void setNotifyFragment(NotifyFragment notifyFragment) {
        this.notifyFragment = notifyFragment;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE; // есть более высокопроизводительный http://qaru.site/questions/14357/viewpager-pageradapter-not-updating-the-view
    }
}