package com.joint.jointpolice.http.exception;

import com.joint.jointpolice.http.api.ApiService;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/8 18:52
 * @描述:
 */

public abstract class CommonResponse<T> extends ResourceSubscriber<T>{


    @Override
    public void onNext(T t) {
        this.onSucc(t);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException excep = (ApiException) e;
            this.onError(excep.getCode(), excep.getMsg());
        } else {
            this.onError(-1,"网络异常,请检查网络连接");
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSucc(T t);

    public abstract void onError(int code,String msg);
}
