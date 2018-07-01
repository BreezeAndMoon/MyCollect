package com.joint.jointpolice.model.CollectModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joint229 on 2018/6/4.
 */

public class Enterprise implements Parcelable {
    public Enterprise(){}
    @SerializedName("SmID")
    private int smID;
    @SerializedName("ModifyDate")
    private String modifyDate;
    @SerializedName("Name")
    private String name;//单位名称
    @SerializedName("Property")
    private String property;//单位性质
    @SerializedName("Type")
    private String type;//单位类型
    @SerializedName("UniformCode")
    private String uniformCode;//统一信用代码
    @SerializedName("LicenseNo")
    private String licenseNo;//营业执照号
    @SerializedName("Ocode")
    private String ocode;//组织机构代码
    @SerializedName("ContactPhone")
    private String contactPhone;//电话
    @SerializedName("Remark")
    private String remark;//备注
    //法人代表信息
    @SerializedName("CorCerType")
    private String corCerType;//证件类型
    @SerializedName("CorCerNo")
    private String corCerNo;
    @SerializedName("CorName")
    private String corName;
    @SerializedName("CorPhone")
    private String corPhone;
    //负责人信息
    @SerializedName("ChargeCerType")
    private String chargeCerType;
    @SerializedName("ChargeCerNo")
    private String chargeCerNo;
    @SerializedName("ChargeName")
    private String chargeName;
    @SerializedName("ChargePhone")
    private String chargePhone;
    //
    @SerializedName("SmUserID")
    private int smUserID;//必填
    @SerializedName("NBuildingID")
    private int nBuildingID;
    @SerializedName("FlatID")
    private int flatID;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(smID);
        dest.writeString(modifyDate);
        dest.writeString(name);
        dest.writeString(property);
        dest.writeString(type);
        dest.writeString(uniformCode);
        dest.writeString(licenseNo);
        dest.writeString(ocode);
        dest.writeString(contactPhone);
        dest.writeString(remark);
        dest.writeString(corCerType);
        dest.writeString(corCerNo);
        dest.writeString(corName);
        dest.writeString(corPhone);
        dest.writeString(chargeCerType);
        dest.writeString(chargeCerNo);
        dest.writeString(chargeName);
        dest.writeString(chargePhone);
        dest.writeInt(smUserID);
        dest.writeInt(nBuildingID);
        dest.writeInt(flatID);
    }

    private Enterprise(Parcel source) {
        smID = source.readInt();
        modifyDate = source.readString();
        name = source.readString();
        property = source.readString();
        type = source.readString();
        uniformCode = source.readString();
        licenseNo = source.readString();
        ocode = source.readString();
        contactPhone = source.readString();
        remark = source.readString();
        corCerType = source.readString();
        corCerNo = source.readString();
        corName = source.readString();
        corPhone = source.readString();
        chargeCerType = source.readString();
        chargeCerNo = source.readString();
        chargeName = source.readString();
        chargePhone = source.readString();
        smUserID = source.readInt();
        nBuildingID = source.readInt();
        flatID = source.readInt();
    }

    public static final Parcelable.Creator<Enterprise> CREATOR = new Creator<Enterprise>() {
        @Override
        public Enterprise createFromParcel(Parcel source) {
            return new Enterprise(source);
        }

        @Override
        public Enterprise[] newArray(int size) {
            return new Enterprise[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUniformCode() {
        return uniformCode;
    }

    public void setUniformCode(String uniformCode) {
        this.uniformCode = uniformCode;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getOcode() {
        return ocode;
    }

    public void setOcode(String ocode) {
        this.ocode = ocode;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCorCerType() {
        return corCerType;
    }

    public void setCorCerType(String corCerType) {
        this.corCerType = corCerType;
    }

    public String getCorCerNo() {
        return corCerNo;
    }

    public void setCorCerNo(String corCerNo) {
        this.corCerNo = corCerNo;
    }

    public String getCorName() {
        return corName;
    }

    public void setCorName(String corName) {
        this.corName = corName;
    }

    public String getCorPhone() {
        return corPhone;
    }

    public void setCorPhone(String corPhone) {
        this.corPhone = corPhone;
    }

    public String getChargeCerType() {
        return chargeCerType;
    }

    public void setChargeCerType(String chargeCerType) {
        this.chargeCerType = chargeCerType;
    }

    public String getChargeCerNo() {
        return chargeCerNo;
    }

    public void setChargeCerNo(String chargeCerNo) {
        this.chargeCerNo = chargeCerNo;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getChargePhone() {
        return chargePhone;
    }

    public void setChargePhone(String chargePhone) {
        this.chargePhone = chargePhone;
    }

    public int getSmUserID() {
        return smUserID;
    }

    public void setSmUserID(int smUserID) {
        this.smUserID = smUserID;
    }

    public int getnBuildingID() {
        return nBuildingID;
    }

    public void setnBuildingID(int nBuildingID) {
        this.nBuildingID = nBuildingID;
    }

    public int getFlatID() {
        return flatID;
    }

    public void setFlatID(int flatID) {
        this.flatID = flatID;
    }

    public int getSmID() {
        return smID;
    }

    public void setSmID(int smID) {
        this.smID = smID;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
