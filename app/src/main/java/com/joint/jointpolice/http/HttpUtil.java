package com.joint.jointpolice.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.hitown.sdk2.http.data.HttpSoapReborn;
import com.hitown.sdk2.http.data.HttpUploadReborn;
import com.hitown.sdk2.http.v2.HttpRestFulQuest;
import com.hitown.sdk2.http.v2.HttpSoapQuest;
import com.hitown.sdk2.http.v2.HttpUploadQuest;
import com.joint.jointpolice.constants.HttpConstant;
import com.joint.jointpolice.constants.SimpleQuest;
import com.joint.jointpolice.http.response.IResponse;
import com.joint.jointpolice.policenet.CommonNet;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

import okhttp3.OkHttpClient;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/29 15:53
 * @描述:
 */

public class HttpUtil  {

    private static Context mContext;
    private static HttpUtil mHttpUtil;

    private IResponse mIResponse;

    private static boolean isPoliceNet=false;


    public IResponse getIResponse() {
        return mIResponse;
    }

    public void setIResponse(IResponse IResponse) {
        mIResponse = IResponse;
    }

    private HttpUtil() {

    }

    public static HttpUtil getInstance() {
        synchronized (HttpUtil.class) {
            if (mHttpUtil == null) {
                mHttpUtil = new HttpUtil();
            }
        }
        return mHttpUtil;
    }

    public HttpUtil(Context context) {
        mContext = context;
    }

    public void doResultGetData() {

    }

    /* 发送参数 */
    public static String doRestfulExample(String UrlParam,String params) {
		/* 设置请求的参数 */
        String message = null;

		/* 返回请求值 */
        if (isPoliceNet) {
            try {
                message = HttpRestFulQuest.questString(mContext, UrlParam, HttpConstant.POST, HttpConstant.AD, params, Charset.forName("utf-8"), HttpConstant.CONTENTTYPE_TEXT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                message = CommonNet.questString(mContext, UrlParam, HttpConstant.POST, params, Charset.forName("utf-8"), HttpConstant.CONTENTTYPE_TEXT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return message;

    }

    /* 发送文件 */
    public void doMultpartUploadQuestExample() {

		/* 请求的参数初始化 */
        HttpUploadQuest httpConn = new HttpUploadQuest();
        /**
         * 修改HttpConstant.NetAD和HttpConstant.HttpUrlPrarm为对应本地值。
         */
        HttpUploadReborn ur = new HttpUploadReborn(mContext, HttpConstant.NetAD, null, "/jwpd/app.do?m=uploadTest", null,
                false, "");
        // 设置自定义的请求参数，请求参数将会待在固定头中转发到应用服务器，无则不填
        ur.AddParam("NAME", "flex");
        ur.AddParam("PASSWORD", "111111");
        ur.AddParam("param1", "param1");
        /** 文件名：文件的路径 */
        File file1 = new File(Environment.getExternalStorageDirectory(), "demo.txt");
        File file2 = new File(Environment.getExternalStorageDirectory(), "demo2.png");
        if (file1.exists()) {
            ur.AddUploadFile("demo1.txt", file1.getAbsolutePath());
            /** 文件名：文件的路径 */
            ur.AddUploadFile("demo2.png", file2.getAbsolutePath());
            try {
			/* 取得返回值 */
                String data = httpConn.UploadFiles(ur, "UTF-8");
                // 把请求结果发到界面上
                Message ms = new Message();
                ms.what = 0;
                Bundle bundleData = new Bundle();
                bundleData.putString("MSG", data);
                ms.setData(bundleData);
//                myHandler.sendMessage(ms);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "文件错误！" + e.getMessage(), Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(mContext, "文件不存在！", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 下载文件
     */
    public void downloadTest() {

		/* 请求的参数初始化 */
        HttpUploadQuest httpConn = new HttpUploadQuest();
        /**
         * 修改HttpConstant.NetAD和HttpConstant.HttpUrlPrarm为对应本地值。
         */
        HttpUploadReborn ur = new HttpUploadReborn(mContext, HttpConstant.NetAD, null, "/jwpd/app.do?m=downloadTest", null,
                false, "");
        ur.AddParam("NAME", "flex");
        ur.AddParam("PASSWORD", "111111");
        ur.AddParam("param1", "param1");
        String saveFilePath = "/storage/sdcard1/demodown.txt";
        try {
			/* 连接服务器 */
            InputStream in = httpConn.Connect(ur, "UTF-8");
            if (in != null) {

                if (!TextUtils.isEmpty(saveFilePath)) {
                    BufferedInputStream bis = new BufferedInputStream(in);
                    File saveFile = new File(saveFilePath);
                    if (!saveFile.exists()) {
                        saveFile.createNewFile();
                    }
                    if (!saveFile.canWrite()) {
                        bis.close();
                        throw new Exception("Save file Error , File CanWrite .");
                    } else {
                        byte[] buffer = new byte[10240];
                        long lbytes = 0;
                        FileOutputStream outFileStream = new FileOutputStream(saveFile);
                        try {
                            while (true) {
                                int len = bis.read(buffer, 0, 10240);
                                if (len < 0) {
                                    break;
                                }
                                lbytes += len;
                                outFileStream.write(buffer, 0, len);
                                buffer = new byte[10240];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new Exception("Save file Error !");
                        } finally {
                            outFileStream.close();
                            bis.close();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* Soap例子 */
    public void doSoapQuestExample() {
		/* 初始化请求参数 */
        /**
         * 修改HttpConstant.SoapNameSpace和HttpConstant.SoapMethodName和HttpConstant
         * .NetAD和HttpConstant.HttpUrlPrarm为对应本地值。
         */
        HttpSoapReborn sb = new HttpSoapReborn(mContext, HttpConstant.SoapNameSpace, HttpConstant.SoapMethodName,
                HttpConstant.NetAD, null, HttpConstant.SoapUrlParam, null, false);
        sb.addProperty("name", "flex");
		/* 请求 */
        try {
            String msg = HttpSoapQuest.quest(sb).toString();
            System.out.print(msg);

            // 把请求结果发到界面上
            Message ms = new Message();
            ms.what = 0;
            Bundle bundleData = new Bundle();
            bundleData.putString("MSG", msg);
            ms.setData(bundleData);
//            myHandler.sendMessage(ms);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 处理请求内容的消息响应
//     */
//    public Handler myHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            mResultTextView.setText(msg.getData().getString("MSG"));
//            super.handleMessage(msg);
//        }
//    };


}
