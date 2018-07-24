package com.joint.jointpolice.util;

import android.text.TextUtils;

import com.joint.jointpolice.app.App;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Joint229 on 2018/7/3.
 */

public class StringUtil {
    public static String formatString(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    public static String formatJsonString(String jsonStr) {
        return jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1).replace("\\\"", "\"");
    }

    public static List<String> getListFromArrayRes(int resID) {
        return Arrays.asList(App.getInstance().getApplicationContext().getResources().getStringArray(resID));
    }

    public static String getHtmlMsg(String s, String startContent, String endContent) {
        String[] id = s.split(startContent);
        String[] idTrue = id[1].split(endContent);
        return idTrue[0];
    }
}
