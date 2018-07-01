package com.joint.jointpolice.util;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.joint.jointpolice.app.App;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/5 15:43
 * @描述:
 */

public final class NetWorkUtil {

    public NetWorkUtil() {

    }

    public static boolean isNetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();

    }

}
