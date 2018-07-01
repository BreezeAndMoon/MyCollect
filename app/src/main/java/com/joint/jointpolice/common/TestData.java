package com.joint.jointpolice.common;

import com.joint.jointpolice.model.UserInfo;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/18 13:36
 * @描述:
 */

public class TestData {
    public static List<UserInfo> setData() {
        List<UserInfo> datas = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("name" + i);
            datas.add(userInfo);
        }
        return datas;
    }

    public static List<UserInfo> setSencondData() {
        List<UserInfo> datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("test" + i);
            datas.add(userInfo);
        }
        return datas;
    }


    public static List<LocalMedia> setLocalMedia(int pictureType,String url) {
        List<LocalMedia> datas = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalMedia localMedia = new LocalMedia();
            localMedia.setMimeType(pictureType);
            localMedia.setDuration(1000);
            localMedia.setPath(url);
            datas.add(localMedia);
        }
        return datas;
    }


}
