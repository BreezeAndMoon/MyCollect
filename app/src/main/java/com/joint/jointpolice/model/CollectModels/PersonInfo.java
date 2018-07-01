package com.joint.jointpolice.model.CollectModels;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Joint229 on 2018/6/8.
 */

public class PersonInfo implements Serializable{
    @SerializedName("Flat")
    private Flat flat;//必须填
    @SerializedName("TableName")
    private String tableName;
    @SerializedName("Person")
    private Person person;

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
