package com.kpfu.khlopunov.sportgid.custom;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class TabViewWrapper {

    private ViewGroup mViewGroup;
    private List<View> mTabList;
    private TabListener mTabListenerClick;
    private int mNotSelectColor = Color.TRANSPARENT;
    private int mSelectColor = Color.TRANSPARENT;
    private int mSelectedPosition;

    public TabViewWrapper(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
        mTabList = new ArrayList<>();
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View child = mViewGroup.getChildAt(i);
            final int position = i;
            child.setOnClickListener(v -> {
                fillView(v);
                mSelectedPosition=position;
                if (mTabListenerClick != null) {
                    mTabListenerClick.onTabClick(position, v);
                }
            });
            mTabList.add(child);
        }
    }

    public void setSelectColor(@ColorRes int selectColor) {
        mSelectColor = ActivityCompat.getColor(mViewGroup.getContext(), selectColor);
        fillView(mSelectedPosition);
    }

    public void setNotSelectColor(@ColorRes int notSelectColor) {
        mNotSelectColor = ActivityCompat.getColor(mViewGroup.getContext(), notSelectColor);
        fillView(mSelectedPosition);
    }

    public void setTabListenerClick(TabListener tabListenerClick) {
        mTabListenerClick = tabListenerClick;
    }

    public void setSelectedTab(int position){
        fillView(position);
        mSelectedPosition=position;
    }

    private void fillView(View tab) {
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View child = mViewGroup.getChildAt(i);
            child.setBackgroundColor(mNotSelectColor);
        }
        tab.setBackgroundColor(mSelectColor);
    }

    private void fillView(int position) {
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View child = mViewGroup.getChildAt(i);
            if (i == position)
                child.setBackgroundColor(mSelectColor);
            else
                child.setBackgroundColor(mNotSelectColor);
        }
    }

    public interface TabListener {
        void onTabClick(int position, View tab);
    }
}
