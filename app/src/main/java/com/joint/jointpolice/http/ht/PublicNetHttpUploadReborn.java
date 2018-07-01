package com.joint.jointpolice.http.ht;

import android.content.Context;

import com.hitown.sdk2.http.data.HttpUploadReborn;

/**
 * Created by Joint229 on 2018/2/11.
 */

public class PublicNetHttpUploadReborn extends HttpUploadReborn {
    private String mUrl;
    public PublicNetHttpUploadReborn(Context context, String AD, String clientAD, String urlPrarm, String userLoginID, boolean isbase64, String filePath,String url){
        super( context,  AD,  clientAD,  urlPrarm,  userLoginID,  isbase64,  filePath);
        this.mheaders.clear();
        mUrl = url;
    }
    @Override
    public String getURL(){
        return mUrl;
    }
}
