package com.joint.jointpolice.http.exception;

import java.io.IOException;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/5 18:35
 * @描述:
 */

public class ApiException extends IOException {

    private int code;
    private String  msg;

   public  ApiException(int code,String msg) {
        super();
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
