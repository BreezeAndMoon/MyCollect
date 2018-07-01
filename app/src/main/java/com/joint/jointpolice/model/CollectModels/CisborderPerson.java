package com.joint.jointpolice.model.CollectModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joint229 on 2018/6/8.
 * 境内人员
 * 暂时不用，三种人员统一放在Person中
 */

public class CisborderPerson implements Serializable {
    @SerializedName("CardNo")
    private String cardNo;//身份证号
    @SerializedName("Telephone")
    private String telephone;//联系方式
    @SerializedName("LiveCause")
    private String liveCause;//居住事由
    @SerializedName("LiveDate")
    private String liveDate;//居住时间
    //居住处所，数据库暂未添加该字段，待添加
    @SerializedName("Sex")
    private String sex;
    @SerializedName("Nature")
    private String nature;//民族
    //同住人员关系
    //省市区县
    @SerializedName("Address")
    private String address;//户籍地详址
    @SerializedName("ServicePlace")
    private String servicePlace;//服务处所
    //职业
    @SerializedName("HealthStatus")
    private String healthStatus;//健康状况
    //下面是所有类型人员共有且必填的字段
    @SerializedName("Status")
    private int status;//未知，但必填
    @SerializedName("PersonTypeID")
    private int personTypeID;//必填
    @SerializedName("Name")
    private String name;//必填
}
