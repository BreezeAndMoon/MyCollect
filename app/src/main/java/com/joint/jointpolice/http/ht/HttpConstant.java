package com.joint.jointpolice.http.ht;

/**
 * Created by Joint229 on 2018/1/17.
 */

public class HttpConstant {
    /**
     * 服务类静态变量。
     */
    /**
     * 应用AD
     */
    public static final String AD = "520052000001000020141230120000";
    /**
     * 用户身份认证
     */
    public static final String UrlParam_UserCheck = "/hitownServer-test/services/userservice/v1/userAuthorize";
    /**
     * 获取警员手机号码信息
     */
    public static final String UrlParam_UserInfo = "/hitownServer-test/services/userservice/v1/getUserMobileInfo";
    /**
     * 获取警员所在单位信息
     */
    public static final String UrlParam_UserUnitInfo = "/hitownServer-test/services/userservice/v1/getUserUnit";
    /**
     * 获取警员所在单位上级单位信息
     */
    public static final String UrlParam_UserParentUnitInfo = "/hitownServer-test/services/userservice/v1/getUserParentUnit";
    /**
     * 获取警员所在单位所有人员信息
     */
    public static final String UrlParam_UnitPersonInfo = "/hitownServer-test/services/userservice/v1/getAllMyUnitUsers";
    /**
     * 向指定警员指定应用推送消息
     */
    public static final String UrlParam_SendMsg2Police = "/hitownServer-test/services/messageservice/v1/sendMsgToSpecifiedAppAndUser";
    /**
     * 向指定单位指定应用的所有警员推送消息
     */
    public static final String UrlParam_SendMsg2UnitPolices = "/hitownServer-test/services/messageservice/v1/sendMsgToSpecifiedAppAndUnit";
    /**
     * 查询消息推送进度
     */
    public static final String UrlParam_MsgProgress = "/hitownServer-test/services/messageservice/v1/askPushMsgReceivedProceed";
    /**
     * 向指定警员发送警务提醒消息
     */
    public static final String UrlParam_SendJingWuManager = "/hitownServer-test/services/messageservice/v1/sendPoliceMsgToSpecifiedUsers";
    /**
     * 向指定单位的所有警员发送警务提醒消息
     */
    public static final String UrlParam_SendJingWuManager4Unit = "/hitownServer-test/services/messageservice/v1/sendPoliceMsgToSpecifiedUnit";
    /**
     * 获取终端LBS位置信息
     */
    public static final String UrlParam_GetLBSLocation = "/hitownServer-test/services/gpsservice/v1/getLBSLocation";
    /**
     * 获取警员最新位置
     */
    public static final String UrlParam_LatestLocation = "/hitownServer-test/services/gpsservice/v1/getUserLatestLocation";
    /**
     * 获取警员历史位置
     */
    public static final String UrlParam_HistoryLocation = "/hitownServer-test/services/gpsservice/v1/getUserHistoryLocation";
    /**
     * 获取指定单位下所有警员最新位置
     */
    public static final String UrlParam_UnitAllUsersLocation = "/hitownServer-test/services/gpsservice/v1/getUnitAllUsersLocation";
    /**
     * 获取指定范围内所有警员最新位置
     */
    public static final String UrlParam_AllScopedUsersLocation = "/hitownServer-test/services/gpsservice/v1/getAllScopedUsersLocation";
    /**
     * 获取指定警员周边范围最新警员位置信息
     */
    public static final String UrlParam_AllNearbyUsersLocation = "/hitownServer-test/services/gpsservice/v1/getAllNearbyUsersLocation";

    /**
     * 网络请求类静态变量
     */
    /**
     * 应用ID
     */
//	public static final String NetAD = "派单";
    //
    public static final String NetAD = "36033603000000001120150721020721";
    /**
     * 上传参数的urlparam
     */
    public static final String HttpUrlPrarm = "/server-test/rest/sayHi";
    /**
     * 上传文件的urlparam
     */
    public static final String UploadFileUrlPrarm = "/server-test/upload/sayHi";
    /**
     * webservice的urlparam
     */
    public static final String SoapUrlParam = "/server-test/ws/wsservice?wsdl";
    /**
     * webservice的空间名称
     */
    public static final String SoapNameSpace = "http://service.webapp.test.org/";
    /**
     * webservice的函数
     */
    public static final String SoapMethodName = "sayHello";

    public static final String HttpUrlPrarm_jwpd = "/jwpd/app.do?m=pushTest";
}
