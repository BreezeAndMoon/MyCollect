package com.joint.jointpolice.model.CollectModels;

import android.support.v4.media.session.MediaSessionCompat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Joint229 on 2018/4/16.
 */

public class Person implements Serializable {
    /*下面是所有类型人员共有字段*/
    @SerializedName("Status")
    private int status;//未知，但必填
    @SerializedName("PersonTypeID")
    private int personTypeID;//必填
    @SerializedName("Name")
    private String name;//必填
    @SerializedName("ID")
    private int id;
    /*境内 */
    @SerializedName("ModifyDate")
    private String modifyDate;//修改日期
    @SerializedName("CardNo")
    private String cardNo;//身份证号
    @SerializedName("Telephone")
    private String telephone;//联系方式
    @SerializedName("LiveCause")
    private String liveCause;//居住事由
    @SerializedName("LiveDate")
    private String liveDate;//居住时间
    @SerializedName("LivePlace")//居住处所
    private String livePlace;
    @SerializedName("Sex")
    private String sex;
    @SerializedName("Nature")
    private String nature;//民族
    @SerializedName("RoommateRelation")//同住人员关系
    private String roommateRelation;
    @SerializedName("ProvinceAndCity")//省市区县
    private String provinceAndCity;
    @SerializedName("DetailNativeAdd")//户籍地详址
    private String detailNativeAdd;
    @SerializedName("ServicePlace")
    private String servicePlace;//服务处所
    @SerializedName("Profession")//职业
    private String profession;
    @SerializedName("HealthStatus")
    private String healthStatus;//健康状况
    /*无户人员*/
    @SerializedName("BirthDate")
    private String birthDate;//出生日期
    @SerializedName("BeforeUseName")
    private String beforeUseName;//曽用名
    @SerializedName("Education")
    private String education;//文化程度
    @SerializedName("NativePlace")
    private String nativePlace;//籍贯
    @SerializedName("County")
    private String county;//国籍/地区
    @SerializedName("Marry")
    private String marry;//婚姻状况
    @SerializedName("Address")
    private String address;//现居地址
    @SerializedName("OldNativeAdd")//原籍地址
    private String oldNativeAdd;
    @SerializedName("GuarderID")//(监护人)身份证号
    private String guarderID;
    @SerializedName("GuarderName")//(监护人)姓名
    private String guarderName;
    @SerializedName("GuarderPhone")//(监护人)联系电话
    private String guarderPhone;
    @SerializedName("GuarderRelation")//(监护人)关系
    private String guarderRelation;
    @SerializedName("GuarderNativeAdd")//(监护人)户籍地址
    private String guarderNativeAdd;
    @SerializedName("NodoorType")//无户类别
    private String nodoorType;
    @SerializedName("NodoorCause")//无户原因
    private String nodoorCause;

    /*境外人员*/
    @SerializedName("PersonCategory")
    private String personCategory;//人员类型
    @SerializedName("CertificateType")//证件类型
    private String certificateType;
    @SerializedName("CertificateNo")//证件号码
    private String certificateNo;
    @SerializedName("LiveMonth")//居住月份(是否大于6个月)
    private boolean liveMonth;
    @SerializedName("SurNameEn")
    private String surNameEn;//英文姓
    @SerializedName("NameEn")
    private String nameEn;//英文名
    //中文姓名即公用的Name
    @SerializedName("StopExpirationDate")
    private String stopExpirationDate;//停留有效期限
    @SerializedName("TempLiveReason")//暂住事由
    private String tempLiveReason;
    @SerializedName("BirthPlace")
    private String birthPlace;//出生地
    @SerializedName("PaperExpirationDate")
    private String paperExpirationDate;//证件有效期限
    @SerializedName("EnterCountryDate")
    private String enterCountryDate;//入境时间
    @SerializedName("EnterCountryPlace")
    private String enterCountryPlace;//入境口岸
    @SerializedName("MessageType")
    private String messageType;//即时通讯类型
    @SerializedName("MessageNo")//即时通讯账号
    private String messageNo;
    @SerializedName("LandlordRelation")//与房东关系
    private String landlordRelation;
    //邀请单位相关字段:
    @SerializedName("UnitName")//单位名称
    private String unitName;
    @SerializedName("UnitContactPerson")//单位联系人
    private String unitContactPerson;
    @SerializedName("UnitContactPhone")//单位联系人电话
    private String unitContactPhone;
    @SerializedName("UnitContactAdd")//单位联系地址
    private String unitContactAdd;
    @SerializedName("Remark")//备注
    private String remark;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPersonTypeID() {
        return personTypeID;
    }

    public void setPersonTypeID(int personTypeID) {
        this.personTypeID = personTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLiveCause() {
        return liveCause;
    }

    public void setLiveCause(String liveCause) {
        this.liveCause = liveCause;
    }

    public String getLiveDate() {
        return liveDate;
    }

    public void setLiveDate(String liveDate) {
        this.liveDate = liveDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getServicePlace() {
        return servicePlace;
    }

    public void setServicePlace(String servicePlace) {
        this.servicePlace = servicePlace;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBeforeUseName() {
        return beforeUseName;
    }

    public void setBeforeUseName(String beforeUseName) {
        this.beforeUseName = beforeUseName;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getMarry() {
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonCategory() {
        return personCategory;
    }

    public void setPersonCategory(String personCategory) {
        this.personCategory = personCategory;
    }

    public String getSurNameEn() {
        return surNameEn;
    }

    public void setSurNameEn(String surNameEn) {
        this.surNameEn = surNameEn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getStopExpirationDate() {
        return stopExpirationDate;
    }

    public void setStopExpirationDate(String stopExpirationDate) {
        this.stopExpirationDate = stopExpirationDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPaperExpirationDate() {
        return paperExpirationDate;
    }

    public void setPaperExpirationDate(String paperExpirationDate) {
        this.paperExpirationDate = paperExpirationDate;
    }

    public String getEnterCountryDate() {
        return enterCountryDate;
    }

    public void setEnterCountryDate(String enterCountryDate) {
        this.enterCountryDate = enterCountryDate;
    }

    public String getEnterCountryPlace() {
        return enterCountryPlace;
    }

    public void setEnterCountryPlace(String enterCountryPlace) {
        this.enterCountryPlace = enterCountryPlace;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getLivePlace() {
        return livePlace;
    }

    public void setLivePlace(String livePlace) {
        this.livePlace = livePlace;
    }

    public String getRoommateRelation() {
        return roommateRelation;
    }

    public void setRoommateRelation(String roommateRelation) {
        this.roommateRelation = roommateRelation;
    }

    public String getProvinceAndCity() {
        return provinceAndCity;
    }

    public void setProvinceAndCity(String provinceAndCity) {
        this.provinceAndCity = provinceAndCity;
    }

    public String getDetailNativeAdd() {
        return detailNativeAdd;
    }

    public void setDetailNativeAdd(String detailNativeAdd) {
        this.detailNativeAdd = detailNativeAdd;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOldNativeAdd() {
        return oldNativeAdd;
    }

    public void setOldNativeAdd(String oldNativeAdd) {
        this.oldNativeAdd = oldNativeAdd;
    }

    public String getGuarderID() {
        return guarderID;
    }

    public void setGuarderID(String guarderID) {
        this.guarderID = guarderID;
    }

    public String getGuarderName() {
        return guarderName;
    }

    public void setGuarderName(String guarderName) {
        this.guarderName = guarderName;
    }

    public String getGuarderPhone() {
        return guarderPhone;
    }

    public void setGuarderPhone(String guarderPhone) {
        this.guarderPhone = guarderPhone;
    }

    public String getGuarderRelation() {
        return guarderRelation;
    }

    public void setGuarderRelation(String guarderRelation) {
        this.guarderRelation = guarderRelation;
    }

    public String getGuarderNativeAdd() {
        return guarderNativeAdd;
    }

    public void setGuarderNativeAdd(String guarderNativeAdd) {
        this.guarderNativeAdd = guarderNativeAdd;
    }

    public String getNodoorType() {
        return nodoorType;
    }

    public void setNodoorType(String nodoorType) {
        this.nodoorType = nodoorType;
    }

    public String getNodoorCause() {
        return nodoorCause;
    }

    public void setNodoorCause(String nodoorCause) {
        this.nodoorCause = nodoorCause;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public boolean isLiveMonth() {
        return liveMonth;
    }

    public void setLiveMonth(boolean liveMonth) {
        this.liveMonth = liveMonth;
    }

    public String getTempLiveReason() {
        return tempLiveReason;
    }

    public void setTempLiveReason(String tempLiveReason) {
        this.tempLiveReason = tempLiveReason;
    }

    public String getMessageNo() {
        return messageNo;
    }

    public void setMessageNo(String messageNo) {
        this.messageNo = messageNo;
    }

    public String getLandlordRelation() {
        return landlordRelation;
    }

    public void setLandlordRelation(String landlordRelation) {
        this.landlordRelation = landlordRelation;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitContactPerson() {
        return unitContactPerson;
    }

    public void setUnitContactPerson(String unitContactPerson) {
        this.unitContactPerson = unitContactPerson;
    }

    public String getUnitContactPhone() {
        return unitContactPhone;
    }

    public void setUnitContactPhone(String unitContactPhone) {
        this.unitContactPhone = unitContactPhone;
    }

    public String getUnitContactAdd() {
        return unitContactAdd;
    }

    public void setUnitContactAdd(String unitContactAdd) {
        this.unitContactAdd = unitContactAdd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
