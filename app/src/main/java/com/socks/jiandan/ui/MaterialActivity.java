package com.socks.jiandan.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.base.JDApplication;
import com.socks.jiandan.ui.fragment.FreshNewsFragment;
import com.socks.jiandan.ui.fragment.PictureFragment;
import com.socks.jiandan.utils.Constants;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by zhaowei on 2016/4/10.
 */
public class MaterialActivity extends BaseActivity {

    private MaterialViewPager materialViewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    private ArrayList<BaseFragment> mFragmentArrayList;
    ArrayList<String> mTitles;
    ArrayList<Integer> mColors;
    private Integer mFragmentSize,mLuckyNum;
    private JSONArray mImgList;

    private SharedPreferences mPrefereneces;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        mPrefereneces = getSharedPreferences(Constants.PREFS_NAME, 0);
        String imgs = mPrefereneces.getString(Constants.HEAD_IMAGES, null);
        Log.d(TAG,"imgs:"+ imgs);
        if (null != imgs){
            mImgList = JSON.parseArray(imgs);

            if (null != mImgList && mImgList.size() > 0 && JDApplication.isWifiConnected()) {
                saveCoverImg();
            }
        }

        setTitle("");

        materialViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        toolbar = materialViewPager.getToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (null != toolbar) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (null != actionBar) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0);

        initView();

    }

    private void saveCoverImg() {

    }

    @Override
    protected void initView() {
        mFragmentArrayList = new ArrayList<>();
        mTitles = new ArrayList<>();
        mColors = new ArrayList<>();

        mFragmentArrayList.add(new FreshNewsFragment());
        mTitles.add(getString(R.string.title_activity_fresh_news_detail));
        mColors.add(R.color.primary);

        mFragmentArrayList.add(new PictureFragment());
        mTitles.add(getString(R.string.title_activity_fresh_picture));
        mColors.add(R.color.primary);

        mFragmentSize = mTitles.size();
        Log.i(TAG, "mFragmentSize:" + mFragmentSize);
        mLuckyNum = new Random().nextInt(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        materialViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentArrayList.get(position % mFragmentSize);
            }

            @Override
            public int getCount() {
                return mFragmentSize;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position % mFragmentSize);
            }
        });

        setViewPageListener();
        materialViewPager.getViewPager().setOffscreenPageLimit(materialViewPager.getViewPager().getAdapter().getCount());
        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());
    }

    private void setViewPageListener() {
        if (mImgList == null || mImgList.size() < mFragmentSize - 1){
            materialViewPager.setMaterialViewPagerListener(page->HeaderDesign.fromColorAndDrawable(
                    mColors.get(page % mFragmentSize),
                    getResources().getDrawable(mColors.get(page % mFragmentSize))));
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void initData() {

    }
}
