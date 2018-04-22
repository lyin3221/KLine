package com.study.lyds.kline.base;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.study.lyds.kline.data.DBHelper;

import org.litepal.LitePal;

import java.util.List;
/**
 * Created by Administrator on 2017/6/8.
 */

public class LyApplication extends Application {
    private static LyApplication _context;

//    static {
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);

        DBHelper.getDbHelper(this);

        _context = this;
    }

    public static LyApplication getInstance() {
        return _context;
    }

}
