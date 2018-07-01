package com.joint.jointpolice.policenet;

import android.content.Context;

import com.hitown.sdk.data.HttpReborn;

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
 * @作者: ChenJunHui
 * @日期: 2018/2/7 15:01
 * @描述:
 */

public class CommonNet {

    public static String questString(Context context, String UrlParam, String method, Object data, Charset charset, String contextType) throws Exception {
        try {
//            HttpReborn httpReborn = new HttpReborn(context, AD);
            URL url = new URL(UrlParam);
            HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
            setRequestPropertys(httpUrlConnection, method, contextType);
//            httpReborn.setUrlParam(UrlParam);

            OutputStream outstream = httpUrlConnection.getOutputStream();
            OutputStreamWriter reader;
            if(data != null) {
                Class<?> mClass = data.getClass();
                if(mClass.equals(String.class)) {
                    reader = new OutputStreamWriter(outstream, charset);
                    reader.write((String)data);
                    reader.flush();
                    reader.close();
                } else if(mClass.equals(byte[].class)) {
                    DataOutputStream mStream = new DataOutputStream(outstream);
                    mStream.write((byte[])data);
                    mStream.flush();
                    mStream.close();
                }
            }

            StringBuilder sb = new StringBuilder();

            try {
                reader = null;
                InputStream istream = httpUrlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(istream, charset));
                char[] read = new char[4096];

                for(int count = bufferedReader.read(read, 0, 4096); count > 0; count = bufferedReader.read(read, 0, 4096)) {
                    String str = new String(read, 0, count);
                    sb.append(str);
                }

                reader.close();
                istream.close();
                reader = null;
                istream = null;
                int mCode = httpUrlConnection.getResponseCode();
                if(mCode != 200) {
                    throw new IOException("HttpStatus异常:" + mCode + "," + sb.toString());
                }
            } catch (Exception var22) {
                var22.printStackTrace();
            } finally {
                if(httpUrlConnection != null) {
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

    private static void setRequestPropertys(HttpURLConnection httpUrlConnection, String method, String contextType) throws Exception {
        if(method != null && method.trim().length() != 0) {
            httpUrlConnection.setRequestMethod(method.toUpperCase());
        } else {
            httpUrlConnection.setRequestMethod("POST");
        }

        if(contextType != null && contextType.trim().length() != 0) {
            httpUrlConnection.setRequestProperty("Content-type", contextType);
        } else {
            httpUrlConnection.setRequestProperty("Content-type", "text/plain");
        }

        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setReadTimeout(1800000);
        httpUrlConnection.setConnectTimeout(3000);
        httpUrlConnection.addRequestProperty("Protocol", "HTTP/1.1");
        httpUrlConnection.setChunkedStreamingMode(4096);
        httpUrlConnection.setRequestProperty("Connection", "Close");
    }

}
