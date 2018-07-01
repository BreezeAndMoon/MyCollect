package com.joint.jointpolice.util;

/**
 * Created by Joint229 on 2018/6/15.
 */

public class Id2StringUtil {
    public static String convertPersonType(int typeId) {
        String personType = "";
        switch (typeId) {
            case 6:
                personType = "境外";
                break;
            case 7:
                personType = "无户";
                break;
            case 8:
                personType = "境内";
                break;
        }
        return personType;
    }
}
