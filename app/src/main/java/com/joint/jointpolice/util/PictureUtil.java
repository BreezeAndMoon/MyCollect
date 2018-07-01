package com.joint.jointpolice.util;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joint229 on 2018/6/1.
 */

public class PictureUtil {
    public static List<LocalMedia> setLocalMedia(List<String> urls) {
        List<LocalMedia> datas = new ArrayList<>();
        for (String url :urls) {
            LocalMedia localMedia = new LocalMedia();
            localMedia.setMimeType(PictureConfig.TYPE_IMAGE);
            localMedia.setDuration(1000);
            localMedia.setPath(url);
            datas.add(localMedia);
        }
        return datas;
    }
}
