package com.joint.jointpolice.util;

import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.util.MutableChar;
import android.widget.Toast;

import com.joint.jointpolice.app.App;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/5 18:58
 * @描述: 常用工具类
 */

public class LUtils {
    public static final String TAG = LUtils.class.getSimpleName();
    public static boolean isDebug = true;
    private static  Context mContext;
    private static Toast mToast;

    public static void log(String msg) {
        log(TAG, msg);
    }

    public static void log(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static  void initialize(Application app) {
      mContext= app.getApplicationContext();
    }

    public static void  toast(CharSequence text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void toast(@StringRes int resId) {
        toast(mContext.getString(resId));
    }



}
