package com.joint.jointpolice.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/8 17:59
 * @描述:
 */

public class UserInfo implements Parcelable {


    private int id;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.userName);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.id = in.readInt();
        this.userName = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
