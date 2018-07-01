package com.joint.jointpolice.util;

/**
 * Created by Joint229 on 2018/1/10.
 */

public class Meters2Degrees {
    public static double metersToDecimalDegrees(double meters, double latitude)
    {
        return meters / (111.32 * 1000 * Math.cos(latitude * (Math.PI / 180)));
    }
}
