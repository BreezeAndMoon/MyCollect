package com.joint.jointpolice.http;

import android.nfc.Tag;

import com.joint.jointpolice.http.api.ApiService;
import com.joint.jointpolice.util.LUtils;
import com.joint.jointpolice.util.NetWorkUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/4 13:40
 * @描述:
 */

public class HttpManager {

    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;
    private static HttpManager mInstance;

    public static HttpManager getInstance() {
        synchronized (HttpManager.class) {
            if (mInstance == null) {
                mInstance = new HttpManager();
            }
        }
        return mInstance;
    }


    public HttpManager() {
        initClient();
        initRetrofit();
    }

    private void initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                LUtils.log("RequestUrl",request.url().toString());
                if (!NetWorkUtil.isNetAvailable()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);

                if (NetWorkUtil.isNetAvailable()) {
                    int maxAge = 0;
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();

                } else {
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        mOkHttpClient = builder.build();

    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(WrapperConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public ApiService provideApiService() {
        return mRetrofit.create(ApiService.class);
    }


}
