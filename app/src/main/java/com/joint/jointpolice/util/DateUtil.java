package com.joint.jointpolice.util;

import android.app.AlarmManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/16 16:53
 * @描述:
 */

public class DateUtil {
    public static Date parseDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            LUtils.log(e.getMessage());
        }
        return date;
    }

    public static String dateStr2Timestamp(String dateStr) {
        if (TextUtils.isEmpty(dateStr))
            return null;
        Date date = parseDate(dateStr);
        String timestamp = String.valueOf((date.getTime()));
        String formatTimestamp = "/Date(" + timestamp + "+0800)/";
        return formatTimestamp;
    }

    public static String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateFormat.format(date);
        return dateStr;
    }

    public static String formatDate(String dateStr) {
        dateStr = dateStr.replace("/Date(", "").replace(")/", "");
        String time = dateStr.substring(0, dateStr.length() - 5);
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static void setSysTime(Context context, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    public static void setSystemTime(final Context cxt, String datetimes) {
        // yyyyMMdd.HHmmss】
        /**
         * 可用busybox 修改时间
         */
            /*
             * String
             * cmd="busybox date  \""+bt_date1.getText().toString()+" "+bt_time1
             * .getText().toString()+"\""; String cmd2="busybox hwclock  -w";
             */
        try {
            Process process = Runtime.getRuntime().exec("su");
            //          String datetime = "20131023.112800"; // 测试的设置的时间【时间格式
            String datetime = ""; // 测试的设置的时间【时间格式
            datetime = datetimes.toString(); // yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            Toast.makeText(cxt, "请获取Root权限", Toast.LENGTH_SHORT).show();
        }
    }


}
