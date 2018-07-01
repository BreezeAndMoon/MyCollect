package com.joint.jointpolice.http.ht;

import android.content.Context;

import com.hitown.sdk.data.HttpReborn;
import com.hitown.sdk2.http.data.HttpRestFulReborn;
import com.hitown.sdk2.http.v2.HttpRestFulQuest;
import com.joint.jointpolice.constants.HttpConstant;

import org.apache.http.HttpConnection;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.http.NameValuePair;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Joint229 on 2018/3/22.
 */

public class MyHttpRestFulQuest  {

    public static Object quest( Context context,String urlParam,String method, Object data,Charset charset, String contentType, String className) throws Exception {
        Object obj = null;
        String strResultData = quest(context,urlParam,method,data,charset, contentType);
        ObjectMapper objmapper = new ObjectMapper();
        obj = objmapper.readValue(strResultData, Class.forName(className));
        return obj;
    }
    private static String quest(Context context, String UrlParam, String method, Object data, Charset charset,String contentType) {
        try {
            HttpURLConnection httpUrlConnection=null;
            if(HtHttpApi.isPoliceNet){
                HttpReborn httpReborn = new HttpReborn(context, HttpConstant.AD);
                URL url = new URL(httpReborn.getAccessUrl());
                 httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpReborn.setUrlParam(UrlParam);
                ArrayList<NameValuePair> nvps = httpReborn.getHeaders();
                Iterator var12 = nvps.iterator();
                while (var12.hasNext()) {
                    NameValuePair nvp = (NameValuePair) var12.next();
                    httpUrlConnection.setRequestProperty(nvp.getName(), nvp.getValue());
                }
            }else{
                URL url = new URL(HtHttpApi.iPAndPort.concat(UrlParam));
                httpUrlConnection =(HttpURLConnection) url.openConnection();
                //
            }
            setRequestPropertys(httpUrlConnection, method,contentType);
            OutputStreamWriter reader;
            if (method.toUpperCase() == "POST") {
                OutputStream outstream = httpUrlConnection.getOutputStream();
                if (data != null) {
                    Class<?> mClass = data.getClass();
                    if (mClass.equals(String.class)) {
                        reader = new OutputStreamWriter(outstream,charset);//加不加charset皆可
                        reader.write((String) data);
                        reader.flush();
                        reader.close();
                    } else if (mClass.equals(byte[].class)) {
                        DataOutputStream mStream = new DataOutputStream(outstream);
                        mStream.write((byte[]) data);
                        mStream.flush();
                        mStream.close();
                    }
                }
            }


            StringBuilder sb = new StringBuilder();

            try {
                reader = null;
                InputStream istream = httpUrlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(istream,charset));// 加不加charset参数皆可
                char[] read = new char[4096];

                for (int count = bufferedReader.read(read, 0, 4096); count > 0; count = bufferedReader.read(read, 0, 4096)) {
                    String str = new String(read, 0, count);
                    sb.append(str);
                }

                bufferedReader.close();
                istream.close();
                bufferedReader = null;
                istream = null;
                int mCode = httpUrlConnection.getResponseCode();
                if (mCode != 200) {
                    throw new IOException("HttpStatus异常:" + mCode + "," + sb.toString());
                }
            } catch (Exception var22) {
                var22.printStackTrace();
            } finally {
                if (httpUrlConnection != null) {
                    httpUrlConnection.disconnect();
                    httpUrlConnection = null;
                }

            }

            return sb.toString();
        } catch (Exception var24) {
            var24.printStackTrace();
            return var24.getMessage();
        }
    }
    private static void setRequestPropertys(HttpURLConnection httpUrlConnection, String method,String contentType) throws Exception {
        httpUrlConnection.setReadTimeout(5000);
        httpUrlConnection.setConnectTimeout(5000);
        httpUrlConnection.setDoInput(true);//默认为true
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setRequestProperty("Connection", "Close");
        httpUrlConnection.setRequestProperty("Content-Type",contentType );//"application/json;charset=UTF-8"
        if (method != null && method.trim().length() > 0 && method.toUpperCase().equals("GET")) {
            httpUrlConnection.setRequestMethod(method);
            httpUrlConnection.setDoOutput(false);//post请求时必须为true,默认false
        } else {
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);
        }
    }
}
