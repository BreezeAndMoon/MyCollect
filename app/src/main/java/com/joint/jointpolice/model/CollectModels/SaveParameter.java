package com.joint.jointpolice.model.CollectModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Joint229 on 2018/6/6.
 */

public class SaveParameter<T> {
    private T item;
    private String personType;
    private int userID;
    public List<FileModel> getToUploadFiles() {
        return toUploadFiles;
    }

    public void setToUploadFiles(List<FileModel> toUploadFiles) {
        this.toUploadFiles = toUploadFiles;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private List<FileModel> toUploadFiles;

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

}
