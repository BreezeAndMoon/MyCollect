package com.joint.jointpolice.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.joint.jointpolice.R;
import com.joint.jointpolice.app.App;
import com.joint.jointpolice.http.OkHttpClientManager;
import com.joint.jointpolice.model.CollectModels.UserHistoricalTrack;
import com.joint.jointpolice.util.LUtils;

import java.util.List;

public class UploadLocationService extends Service {
    private Gson mGson = new Gson();
    private LocationManager locationManager;
    private String locationProvider;

    public UploadLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //todo 获取sendrate
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Location location = getLocation();
        if (location!=null) {
            OkHttpClientManager.postAsyn(getResources().getString(R.string.save_historical_track), buildData(0, 0), new OkHttpClientManager.ResultCallback() {
                @Override
                public void onResponse(Object response) {
                    // do nothing
                }
            });
        }
        alarmTask();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void alarmTask() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent newIntent = new Intent(this, UploadLocationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, newIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        }
    }

    private String buildData(double lat, double lng) {
        UserHistoricalTrack userHistoricalTrack = new UserHistoricalTrack(App.UserID);
        userHistoricalTrack.setLat(lat);
        userHistoricalTrack.setLng(lng);
        //todo 设置createdate
        return mGson.toJson(userHistoricalTrack);
    }

    private Location getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //todo 申请权限
            LUtils.toast("未开启位置访问权限");
            return null;
        } else {
            setLocationProvider();
            if (locationProvider != null) {
                Location location = locationManager.getLastKnownLocation(locationProvider);
                return location;
            } else return null;
        }
    }

    private void setLocationProvider() {
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            LUtils.toast("不能获取当前位置");
            locationProvider = null;
        }
    }
}
