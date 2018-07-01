package com.joint.jointpolice.model.CollectModels;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Joint229 on 2018/5/30.
 */

public class SaveFlatParameter implements Serializable {
    private Flat item;
    private int userID;

    public List<FileModel> getToUploadFiles() {
        return toUploadFiles;
    }

    public void setToUploadFiles(List<FileModel> toUploadFiles) {
        this.toUploadFiles = toUploadFiles;
    }

    private List<FileModel> toUploadFiles;

    public Flat getItem() {
        return item;
    }

    public void setItem(Flat item) {
        this.item = item;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
