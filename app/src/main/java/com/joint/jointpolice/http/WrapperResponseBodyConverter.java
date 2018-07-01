package com.joint.jointpolice.http;

import android.nfc.Tag;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.joint.jointpolice.http.exception.ApiException;
import com.joint.jointpolice.util.LUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/5 18:10
 * @描述:
 */

public class WrapperResponseBodyConverter<T> implements  Converter<ResponseBody,T> {

    private static  final String TAG = WrapperResponseBodyConverter.class.getSimpleName();
    private Type mType;
    WrapperResponseBodyConverter(Type type) {
       this. mType = type;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String result=value.string();
            LUtils.log(TAG,result);
            if (TextUtils.isEmpty(result)||result.contains("error")) {
                throw new ApiException(0,"网络异常,请检查网络连接");
            }
            Object object = new JSONTokener(result).nextValue();
            if (object instanceof JSONObject) {
                JSONObject data = (JSONObject) object;
                if(data.has("code")&& data.has("message")){
                    int code=data.getInt("code");
                    if (code != 1) {
                        throw new ApiException(code,data.getString("message"));
                    }
                    if(data.has("data")&&!data.isNull("data")){
                        result = data.opt("data").toString();
                    }

                }
            }
            return new Gson().fromJson(result,mType);

        } catch (JSONException | JsonParseException e) {
            throw new ApiException(0,"数据解析错误");
        }

    }
}
