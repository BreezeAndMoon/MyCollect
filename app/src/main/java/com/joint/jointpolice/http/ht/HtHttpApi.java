package com.joint.jointpolice.http.ht;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hitown.sdk2.http.data.HttpRestFulReborn;
import com.hitown.sdk2.http.data.HttpUploadReborn;
import com.hitown.sdk2.http.v2.HttpRestFulQuest;
import com.hitown.sdk2.http.v2.HttpUploadQuest;
import com.joint.jointpolice.activity.LoginActivity;
import com.joint.jointpolice.app.App;
import com.joint.jointpolice.constants.HttpConstant;
import com.joint.jointpolice.http.exception.ApiException;
import com.joint.jointpolice.http.response.IResponse;
import com.joint.jointpolice.util.LUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joint229 on 2018/1/17.
 */

public class HtHttpApi {
    /**
     * 外网测试时，需要修改对应的服务器ip和端口 （内网和外网模式的切换开关。）
     */
    public static final boolean isPoliceNet = false;
    public static final String iPAndPort = "http://117.29.170.182:8082/";
    //    public static final String iPAndPort = "http://117.29.170.182:8081/HouseServices/PoliceDataService.svc/";
    //public static final String iPAndPort = "http://117.29.170.182:8082/ServiceTest/StudentService.svc/";
    public static final int HANDLE_SHOW_MSG = 0x000003;

    private TextView mResultTextView;
    private static final String TAG = "HtHttpApi";

    /**
     * 已弃用
     * @param urlParam
     * @param param     请求的参数对象
     * @param className
     * @param method    只针对外网有效，HT封装的只支持POST
     * @return
     * @throws Exception
     */
    public static Object restfulRequest(String urlParam, Object param, String className, String method, String contentType) {
        try {
            Charset charset = Charset.forName("utf-8");//默认有设置
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(param);
            if (isPoliceNet) {
                HttpRestFulReborn httpRestFulReborn = new HttpRestFulReborn(App.getInstance().getApplicationContext(), HttpConstant.AD);
                httpRestFulReborn.setData(data);
                httpRestFulReborn.setUrlParam(urlParam);
                Object result = HttpRestFulQuest.quest(httpRestFulReborn, contentType, className);//注by付益铭:UrlParam最终通过HttpConnection的setRequestProperty()添加到请求头（自定义的请求头，用来传递参数）中，而data则是通过常见的post方式写入到outStream来作为参数传递
                return result;
            } else {
                URL url = new URL(iPAndPort.concat(urlParam));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setDoInput(true);//默认为true
                conn.setUseCaches(false);
                conn.setRequestProperty("Connection", "Close");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                if (method != null && method.trim().length() > 0 && method.toUpperCase().equals("GET")) {
                    conn.setRequestMethod(method);
                    conn.setDoOutput(false);//post请求时必须为true,默认false
                } else {
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    OutputStream out = conn.getOutputStream();//不支持GET方法，会抛异常
                    out.write(data.getBytes());
                    out.flush();
                    out.close();
                }
                //String data = "{\"Id\":4,\"Name\":\"王六\"}";
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    String stringResult = inputStream2String(in);
                    Object result = mapper.readValue(stringResult, Class.forName(className));

                    return result;
                } else {
                    int code = conn.getResponseCode();
                    Log.i("ResponseCode", Integer.toString(code));
                    //throw new ApiException(0, "网络异常,请检查网络连接");
                }
            }

        } catch (Exception e) {
            //new ApiException(0, "数据异常");
            LUtils.log(e.getMessage());
        }

        return null;
    }

    public static Object restfulQuest(Context context, String urlParam, String method, Object data, Charset charset, String contentType, String className) {
        if (charset == null)
            charset = Charset.forName("utf-8");
        try {
            return MyHttpRestFulQuest.quest(context, urlParam, method, data, charset, contentType, className);
        } catch (Exception e) {
            LUtils.log(TAG, e.getMessage());
            return null;
        }
    }

    /* 发送文件 */
    public static String uploadQuest(String urlParam, List<String> filePaths, Map<String, String> paramMap) throws Exception {
        //String htUploadTestUrlParam ="/jwpd/app.do?m=uploadTest";
        HttpUploadReborn ur;
        HttpUploadQuest httpConn = new HttpUploadQuest();
        if (isPoliceNet) {
            /**
             * 修改HttpConstant.NetAD和HttpConstant.HttpUrlPrarm为对应本地值。
             */
            ur = new HttpUploadReborn(App.getInstance().getApplicationContext(), HttpConstant.NetAD, null, urlParam, null,
                    false, "");            // 陈志斌说俩ad参数填一样。。
        } else {
            //ur = new PublicNetHttpUploadReborn(App.getInstance().getApplicationContext(), HttpConstant.NetAD, null, urlParam, null,
                    //false, "", iPAndPort.concat(urlParam));
            ur = new PublicNetHttpUploadReborn(App.getInstance().getApplicationContext(), HttpConstant.NetAD, null, urlParam, null,
            false, "", urlParam);
        }
        // 设置自定义的请求参数，请求参数将会待在固定头中转发到应用服务器，无则不填
        //注by付益铭:这些参数最终会转成一个json串添加到mheader(key为PARAMETERS)中然后拼接成表单数据格式，并添加到outStream中，此外，具体的文件流也会被一起添加到outStream中
        if(paramMap!=null) {
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                ur.AddParam(entry.getKey(), entry.getValue());
            }
        }
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (file.exists())
            /**文件名：文件的路径 */
                ur.AddUploadFile(file.getName(), file.getAbsolutePath());
            else
                throw new Exception("文件不存在");
        }
         /* 取得返回值 */
        String result = httpConn.UploadFiles(ur, "UTF-8");
        return result;
    }

    /**
     * 下载文件
     */
    public static void downloadQuest(String urlParam, String saveFilePath, Map<String, String> paramMap) {
//String htDownloadTestUrlParam ="/jwpd/app.do?m=uploadTest";
        /* 请求的参数初始化 */
        HttpUploadQuest httpConn = new HttpUploadQuest();
        HttpUploadReborn ur;
        if (isPoliceNet) {
            /**
             * 修改HttpConstant.NetAD和HttpConstant.HttpUrlPrarm为对应本地值。
             */
            ur = new HttpUploadReborn(App.getInstance().getApplicationContext(), HttpConstant.NetAD, null, urlParam, null,
                    false, "");            // 陈志斌说俩ad参数填一样。。
        } else {
            ur = new PublicNetHttpUploadReborn(App.getInstance().getApplicationContext(), HttpConstant.NetAD, null, urlParam, null,
                    false, "", iPAndPort.concat(urlParam));
        }
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            ur.AddParam(entry.getKey(), entry.getValue());
        }
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


    /**
     * 处理请求内容的消息响应
     */
    public Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            mResultTextView.setText(msg.getData().getString("MSG"));
            super.handleMessage(msg);
        }
    };

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }
    public static void myUploadTest(String urlString,String fileName) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(150000);
        conn.setConnectTimeout(150000);
        conn.setDoInput(true);//默认为true
        conn.setUseCaches(false);
        conn.setRequestProperty("Connection", "Close");
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        FileInputStream inputStream = new FileInputStream(new File(fileName));
        byte[] b = new byte[1024*4];
        int len;
        while((len = inputStream.read(b))!= -1){
            out.write(b,0,len);
        }
//        if (conn.getResponseCode() == 200) {

                    InputStream in = null;
                    try {
                        in = conn.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String stringResult = inputStream2String(in);
                        LUtils.toast(stringResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



//    }
    public static void httpUpload(String url,String fileName){
        String boundary = "----WebKitFormBoundaryP0Rfzlf32iRoMhmb";
        //实际用到的时候会多两个杠杠          ------WebKitFormBoundaryP0Rfzlf32iRoMhmb
        String prefix = "--";
        String end = "\r\n";
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //通过multipart这种编码方式上传文件
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            //拼装要上传的文件
            out.writeBytes(prefix+boundary+end);
            out.writeBytes("Content-Disposition: form-data;"
                    +"name=\"file\";filename=\""+"test.jpg" +"\""+end);
            out.writeBytes(end);
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            byte[] b = new byte[1024*4];
            int len;
            while((len = inputStream.read(b))!= -1){
                out.write(b,0,len);
            }
            out.writeBytes(end);
            out.writeBytes(prefix+boundary+prefix+end);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str = null;
            StringBuffer sb = new StringBuffer();
            while((str = bufferedReader.readLine())!= null){
                sb.append(str);
            }
            if(out != null){
                out.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
