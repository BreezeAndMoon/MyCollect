package com.joint.jointpolice.model.CollectModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joint229 on 2018/5/31.
 */

public class FileModel {
    @SerializedName("Extension")
    private String extension;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    @SerializedName("RelativePath")
    private String relativePath;
}
