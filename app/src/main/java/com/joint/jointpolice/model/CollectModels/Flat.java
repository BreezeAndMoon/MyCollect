package com.joint.jointpolice.model.CollectModels;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Joint229 on 2018/5/25.
 */

public class Flat implements Serializable {
    @SerializedName("ModifyDate")
    private String modifyDate;
    @SerializedName("HouseStructure")
    private String houseStructure;//房屋结构
    @SerializedName("HouseType")
    private String houseType;//房屋类型
    @SerializedName("LocationName")
    private String locationName;//地址名
    @SerializedName("HouseNature")
    private String houseNature;//房屋性质
    @SerializedName("HouseStatus")
    private String houseStatus;//房屋现状
    @SerializedName("HousePurpose")
    private String housePurpose;//房屋用途
    @SerializedName("IsRent")
    private boolean isRent;//是否出租屋
    @SerializedName("StandardAd")//标准地址
    private String standardAd;
    @SerializedName("LandlordCerType")//房东证件类型
    private String landlordCerType;
    @SerializedName("LandlordCerNo")//证件号码
    private String landlordCerNo;
    @SerializedName("LandlordName")//中文姓名
    private String landlordName;
    @SerializedName("LandlordPhone")//联系电话
    private String landlordPhone;
    @SerializedName("AgentCerType")//代管人类型
    private String agentCerType;
    @SerializedName("AgentCerNo")
    private String agentCerNo;
    @SerializedName("AgentName")
    private String agentName;
    @SerializedName("AgentPhone")
    private String agentPhone;
    @SerializedName("AttentionState")//关注情况
    private String attentionState;

    @SerializedName("Floor_Form")
    private String floor_Form;
    @SerializedName("Floor_To")
    private String floor_To;
    @SerializedName("BuildingID")
    private String buildingID;
    @SerializedName("RoomNumber")
    private String roomNumber;
    @SerializedName("RoomNumberShown")
    private String roomNumberShown;
    @SerializedName("Area")
    private String area;
    @SerializedName("ID")
    private long id;

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getHousePurpose() {
        return housePurpose;
    }

    public void setHousePurpose(String housePurpose) {
        this.housePurpose = housePurpose;
    }

    public boolean getIsRent() {
        return isRent;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setIsRent(boolean isRent) {
        this.isRent = isRent;
    }

    public String getFloor_Form() {
        return floor_Form;
    }

    public void setFloor_Form(String floor_Form) {
        this.floor_Form = floor_Form;
    }

    public String getFloor_To() {
        return floor_To;
    }

    public void setFloor_To(String floor_To) {
        this.floor_To = floor_To;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumberShown() {
        return roomNumberShown;
    }

    public void setRoomNumberShown(String roomNumberShown) {
        this.roomNumberShown = roomNumberShown;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseStructure() {
        return houseStructure;
    }

    public void setHouseStructure(String houseStructure) {
        this.houseStructure = houseStructure;
    }

    public String getHouseNature() {
        return houseNature;
    }

    public void setHouseNature(String houseNature) {
        this.houseNature = houseNature;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String address) {
        this.locationName = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRent() {
        return isRent;
    }

    public void setRent(boolean rent) {
        isRent = rent;
    }

    public String getStandardAd() {
        return standardAd;
    }

    public void setStandardAd(String standardAd) {
        this.standardAd = standardAd;
    }

    public String getLandlordCerType() {
        return landlordCerType;
    }

    public void setLandlordCerType(String landlordCerType) {
        this.landlordCerType = landlordCerType;
    }

    public String getLandlordCerNo() {
        return landlordCerNo;
    }

    public void setLandlordCerNo(String landlordCerNo) {
        this.landlordCerNo = landlordCerNo;
    }

    public String getLandlordName() {
        return landlordName;
    }

    public void setLandlordName(String landlordName) {
        this.landlordName = landlordName;
    }

    public String getLandlordPhone() {
        return landlordPhone;
    }

    public void setLandlordPhone(String landlordPhone) {
        this.landlordPhone = landlordPhone;
    }

    public String getAgentCerType() {
        return agentCerType;
    }

    public void setAgentCerType(String agentCerType) {
        this.agentCerType = agentCerType;
    }

    public String getAgentCerNo() {
        return agentCerNo;
    }

    public void setAgentCerNo(String agentCerNo) {
        this.agentCerNo = agentCerNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public String getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(String attentionState) {
        this.attentionState = attentionState;
    }
}
