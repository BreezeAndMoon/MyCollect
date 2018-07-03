package com.joint.jointpolice.util;

import android.text.TextUtils;

/**
 * Created by Joint229 on 2018/7/3.
 */

public class StringUtil {
    public static String formatString(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }
}
