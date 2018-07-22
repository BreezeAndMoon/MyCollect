package com.joint.jointpolice.model.CollectModels;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fym on 2018/7/22.
 */

public class UserHistoricalTrack {
    @SerializedName("ID")
    private int id;
    @SerializedName("UserID")
    private int UserID;
    @SerializedName("CreateTime")
    private String CreateTime;
    @SerializedName("Lng")
    private double Lng;
    @SerializedName("Lat")
    private double Lat;

    public UserHistoricalTrack(int userID) {
        UserID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public double getLng() {
        return Lng;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }
}
