package com.joint.jointpolice.http;

import android.support.annotation.Nullable;
import android.telephony.gsm.GsmCellLocation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/5 16:30
 * @描述:
 */

public class WrapperConverterFactory extends Converter.Factory {

    private Gson mGson;


    public static  WrapperConverterFactory create() {

        Gson mGson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong()*1000);
            }
        }).create();
        return create(mGson);
    }
    private static WrapperConverterFactory create(Gson gson) {

        return new WrapperConverterFactory(gson);

    }

    private WrapperConverterFactory(Gson gson) {

        if (gson == null) throw new NullPointerException("gson==null");
        this.mGson = gson;

    }


    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new WrapperResponseBodyConverter<>(type);
    }


}
