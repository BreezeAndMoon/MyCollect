package com.joint.jointpolice.model.CollectModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joint229 on 2018/3/30.
 */

public class NBuilding {
    @SerializedName("Address")
    private String address;
    @SerializedName("Community")
    private String community;
    @SerializedName("HouseUseID")
    private int houseUseID;
    @SerializedName("Name")
    private String name;
    @SerializedName("SmID")
    private int smID;
    @SerializedName("StandardAd")
    private String standardAd;
    @SerializedName("Staus")
    private int status;
    @SerializedName("SmX")
    private double smX;
    @SerializedName("SmY")
    private double smY;

    public String getAddress() {
        return address;
    }

    public String getCommunity() {
        return community;
    }

    public int getHouseUseID() {
        return houseUseID;
    }

    public String getName() {
        return name;
    }

    public int getSmID() {
        return smID;
    }

    public String getStandardAd() {
        return standardAd;
    }

    public int getStatus() {
        return status;
    }

    public double getSmX() {
        return smX;
    }

    public double getSmY() {
        return smY;
    }


}
