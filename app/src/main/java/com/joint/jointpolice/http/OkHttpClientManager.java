package com.joint.jointpolice.http;

import android.app.Activity;
import android.app.Application;
import android.mtp.MtpConstants;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.load.HttpException;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.joint.jointpolice.app.App;
import com.joint.jointpolice.common.BaseActivity;
import com.joint.jointpolice.constants.HttpConstant;
import com.joint.jointpolice.util.LUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.FileNameMap;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

/**
 * Created by Joint229 on 2018/5/24.
 */

public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    private Handler mDelivery;

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                mInstance = new OkHttpClientManager();
            }
        }
        return mInstance;
    }

    /**
     * post异步请求
     *
     * @param url
     * @param jsonStr
     */
    public static void postAsyn(String url, String jsonStr, ResultCallback resultCallback) {
        Request request = getInstance().buildPostRequest(url, jsonStr);
        if (request != null)
            getInstance().deliveryResult(request, resultCallback);
    }

    public static void postFileAsyn(String url, String filePath, ResultCallback resultCallback) {
        Request request = getInstance().buildFilePostRequest(url, filePath);
        if (request != null) {
            getInstance().deliveryResult(request, resultCallback);
        }
    }

    public static void postFormDataAsync(String filePath, ResultCallback resultCallback) {
        Request request = getInstance().buildFormPostRequest(filePath);
        if (request != null) {
            getInstance().deliveryResult(request, resultCallback);
        }
    }

    /**
     * 构建请求
     *
     * @param url
     * @param jsonStr
     * @return
     */
    private Request buildPostRequest(String url, String jsonStr) {
        RequestBody requestBody;
        if (jsonStr == null)
            requestBody = Util.EMPTY_REQUEST;
        else
            requestBody = RequestBody.create(MediaType.parse(HttpConstant.CONTENTTYPE_JSON), jsonStr);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    private Request buildFilePostRequest(String url, String filePath) {
        if (TextUtils.isEmpty(filePath))
            return null;
        File file = new File(filePath);
        if (!file.exists()) {
            LUtils.toast("文件不存在");
            return null;
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            return request;
        }
    }

    private Request buildFormPostRequest(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return null;
        File file = new File(filePath);
        long i = file.length();
        LUtils.log(String.valueOf(i));
        //构造上传请求，类似web表单
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"callbackurl\""), RequestBody.create(null, "/idcard/"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"action\""), RequestBody.create(null, "idcard"))
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"img\"; filename=\"idcardFront_user.jpg\""), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();
        //进行包装，使其支持进度回调
        final Request request = new Request.Builder()
                .header("Upgrade-Insecure-Requests", "1")
                .header("Accept-Encoding", "gzip,deflate")
                .header("Host", "ocr.ccyunmai.com:8080")
                .header("Origin", "http://ocr.ccyunmai.com:8080")
                .header("Referer", "http://ocr.ccyunmai.com:8080/idcard/")
                .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2398.0 Safari/537.36")
                .url("http://ocr.ccyunmai.com:8080/UploadImage.action")
                .post(requestBody)
                .build();
        return request;
    }


    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private void deliveryResult(Request request, final ResultCallback callback) {
        callback.onBefore();//请求前(主线程)
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                sendFailedStringCallback(call.request(), e, callback);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (response.code() != 200)
                        throw new Exception("response.code()!=200");
                    final String string = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else if (callback.mType == null) {
                        sendSuccessResultCallback(null, callback);
                    } else {
                        Object o = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(request, e, callback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, callback);
                } catch (Exception ex) {
                    sendFailedStringCallback(response.request(), ex, callback);
                }
            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                    callback.onAfter();//请求后
                }
            }
        });
    }

    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                //throw new RuntimeException("Missing type parameter.");
                return null;//服务端没有返回数据时不需要指明泛型参数T，此时就返回null
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public void onError(Request request, Exception e) {
            if (e instanceof SocketTimeoutException) {
                LUtils.toast("连接超时");
            }
            if (e instanceof ConnectException) {
                LUtils.toast("连接异常");
            }
            if (e instanceof com.google.gson.JsonParseException) {
                LUtils.toast("解析异常");
            } else {
                LUtils.toast("请求失败");
            }
            LUtils.log(e.getMessage());//打印异常日志
        }

        public abstract void onResponse(T response);

        public void onBefore() {

        }

        public void onAfter() {
        }
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }
}
