package com.joint.jointpolice.app;

import android.app.Activity;
import android.app.Application;

import com.joint.jointpolice.util.LUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/5 15:41
 * @描述:
 */

public class App extends Application {

    private static App mInstance;
    public static int UserID =1;

    public static App getInstance() {
        synchronized (App.class) {
            if (mInstance == null) {
                mInstance = new App();
            }
        }

        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LUtils.initialize(this);
        LUtils.isDebug = true;
        JPushInterface.init(this);
        JPushInterface.setDebugMode(LUtils.isDebug);
    }


}
