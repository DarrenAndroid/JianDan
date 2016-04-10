package com.socks.jiandan.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.stetho.Stetho;
import com.socks.greendao.DaoMaster;
import com.socks.greendao.DaoSession;
import com.socks.jiandan.BuildConfig;
import com.socks.jiandan.R;
import com.socks.jiandan.cache.BaseCache;
import com.socks.jiandan.utils.StrictModeUtil;
import com.socks.jiandan.utils.logger.LogLevel;
import com.socks.jiandan.utils.logger.Logger;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class JDApplication extends Application {

    public static int COLOR_OF_DIALOG = R.color.primary;
    public static int COLOR_OF_DIALOG_CONTENT = Color.WHITE;

    private static Context mContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private RefWatcher refWatcher;

    public static Resources mResources;

//    private static Toaster sToaster;

    @Override
    public void onCreate() {
        StrictModeUtil.init();
        super.onCreate();
        refWatcher = LeakCanary.install(this);
        mContext = this;
        ImageLoadProxy.initImageLoader(this);
        mResources = mContext.getResources();

        if (BuildConfig.DEBUG) {
            Logger.init().hideThreadInfo().setMethodCount(1).setLogLevel(LogLevel.FULL);
        }

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }

    public static Context getContext() {
        return mContext;
    }

    public static RefWatcher getRefWatcher(Context context) {
        JDApplication application = (JDApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, BaseCache.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected())
            return true;
        else
            return false;
    }
}