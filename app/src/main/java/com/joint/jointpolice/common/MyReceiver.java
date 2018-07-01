package com.joint.jointpolice.common;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.view.animation.AnimationUtils;

import com.joint.jointpolice.fragment.MyFragment;
import com.joint.jointpolice.util.LUtils;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * @作者: ChenJunHui
 * @日期: 2018/3/7 14:09
 * @描述: 极光推送服务
 */

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = MyReceiver.class.getName();
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
        LUtils.log(TAG, "onReceive===" + intent.getAction() + ", extra: " + bundle);

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            LUtils.log(TAG, "jPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LUtils.log(TAG, "接收到推送下来的自定义消息");
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LUtils.log(TAG, "接收推送下来的通知");
            receivingNotification(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LUtils.log(TAG, "打开推送下来的通知");
        } else {
            LUtils.log(TAG, "Unhandled intent - " + intent.getAction());
        }


    }

    private void receivingNotification(Context context, Bundle bundle) {
        String titile = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LUtils.log(TAG, "titile: " + titile);
        LUtils.log(TAG, "message: " + message);
        LUtils.log(TAG, "extra: " + extra);
        EventBus.getDefault().post(message);
    }


}
