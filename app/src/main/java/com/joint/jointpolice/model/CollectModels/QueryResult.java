package com.joint.jointpolice.model.CollectModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Joint229 on 2018/7/6.
 */

public class QueryResult<T> {
    int flatType;
    @SerializedName("Data")
    List<T> data;

    public int getFlatType() {
        return flatType;
    }

    public void setFlatType(int flatType) {
        this.flatType = flatType;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
