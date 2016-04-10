package com.socks.jiandan.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.jiandan.R;
import com.socks.jiandan.base.BaseActivity;
import com.socks.jiandan.utils.Constants;

import java.io.File;
import java.util.Date;

/**
 * Created by zhaowei on 2016/4/10.
 */
public class WelcomeActivity extends BaseActivity {



    private ImageView imageView;
    private TextView textView;
    private Date mStartDate;
    final long WELCOME_TIME = 1500;
    private SharedPreferences mPreferences;

        @Override
       protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        imageView = (ImageView)findViewById(R.id.welcome_image);
        textView = (TextView)findViewById(R.id.welcome_version);

        mStartDate = new Date();
        mPreferences = getSharedPreferences(Constants.PREFS_NAME,0);
        SharedPreferences.Editor editor = mPreferences.edit();
        File file = new File(mPreferences.getString(Constants.COVER_IMAGE, ""));
        if (file.exists()) Glide.with(this).load(file).into(imageView);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            String version = String.format(getResources().getString(R.string.app_version), packageInfo.versionName);
            textView.setText(pass(version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        toMainPage();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void toMainPage() {
        if (getWaitTime() <= 0 ){
            go();
        }else {
            final Handler handler = new Handler();
            Runnable runnable  = new Runnable() {
                @Override
                public void run() {
                    go();
                }
            };
            handler.postDelayed(runnable,getWaitTime());
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void go() {
        Intent intent = new Intent(getApplicationContext(),MaterialActivity.class);
        startActivityForResult(intent, Constants.WELCOME_ACTIVITY);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        try {
            if (this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode >= Build.VERSION_CODES.LOLLIPOP)
                finishAfterTransition();
            else
                finish();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getWaitTime() {
        long waitTime = WELCOME_TIME - (new Date().getTime() - mStartDate.getTime());
        return (int)waitTime;
    }
}
