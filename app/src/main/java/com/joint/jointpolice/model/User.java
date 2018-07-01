package com.joint.jointpolice.model;

import android.support.v4.media.session.MediaSessionCompat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joint229 on 2018/6/25.
 */

public class User {
    @SerializedName("UserName")
    private String userName;
    @SerializedName("Password")
    private String password;
    @SerializedName("ID")
    private int id;
    @SerializedName("UserDisplayName")
    private String userDisplayName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
}
