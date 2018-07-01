package com.joint.jointpolice.http.api;

import com.joint.jointpolice.model.User;
import com.joint.jointpolice.model.UserInfo;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @作者: ChenJunHui
 * @日期: 2018/1/5 16:22
 * @描述:
 */

public interface ApiService {
//    public static final String BASE_URL = "https://www.baidu.com/";
//    public static final String BASE_URL ="http://117.29.170.182:6080/arcgis/rest/services/NingDeShi/ND_ND_IMAGE/MapServer";
//    public static final String BASE_URL ="http://117.29.170.182:8081/HouseServices/PoliceDataService.svc/";
    public static final String BASE_URL ="http://117.29.170.182:8082/ServiceTest/StudentService.svc/";


    /**
     * 用户登录
     */
    @GET("/user/login")
    Flowable<UserInfo> login(
            @Query("tel") String tel,
            @Query("pwd")String pwd
    );

    @GET("/")
    Flowable<String> testDeom(

    );

    @GET("T_User")
    Flowable<String> t_user(

    );

    @GET("getstudentbyid/Id=1")
    Flowable<User> user(

    );

}
