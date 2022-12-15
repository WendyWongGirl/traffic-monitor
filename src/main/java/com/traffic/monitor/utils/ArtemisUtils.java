package com.traffic.monitor.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtemisUtils  {


    private static final Logger logger = LoggerFactory.getLogger(ArtemisUtils.class);


    static {
        // 代理API网关nginx服务器ip端口
        ArtemisConfig.host = "";
        // 秘钥appkey
        ArtemisConfig.appKey = "";
        // 秘钥appSecret
        ArtemisConfig.appSecret = "";
    }

    /**
     * 能力开放平台的网站路径
     */
    private static final String ARTEMIS_PATH = "/artemis";

    /* *//**
     * 订阅报警事件
     *//*
    public static String alarmSubscription() {

        final String previewURLsApi = ARTEMIS_PATH + "/api/eventService/v1/eventSubscriptionByEventTypes";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(HTTP + "://", previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        //订阅事件的类型,防区报警,
        int[] arr = ConstParams.SUBSCRIBE_EVENT_TYPE_ARR;
        jsonBody.put("eventTypes", arr);
        String backUrl = "http://" + ConstParams.CALLBACK_IP + ":" + ConstParams.CALLBACK_PORT + ConstParams.CALLBACK_URL;
        logger.info("事件回调接口:" + backUrl);
        // 接受事件的回调接口
        jsonBody.put("eventDest", backUrl);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
        return result;
    }

    *//**
     * 取消订阅事件
     */
   /* public static String cancelAlarmSubscription() {

        final String previewURLsApi = ARTEMIS_PATH + "/api/eventService/v1/eventUnSubscriptionByEventTypes";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(ConstParams.ARTEMIS_HTTP + "://", previewURLsApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        //取消订阅事件类型
        int[] arr = ConstParams.CANCEL_SUBSCRIBE_EVENT_TYPE_ARR;
        jsonBody.put("eventTypes", arr);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);// post请求application/json类型参数
        return result;
    }*/

    /**
     * 分页获取区域列表
     */
    public static String getAllRegionInfo(int pageNo, int pageSize) {

        String getRootApi = ARTEMIS_PATH + "/api/resource/v1/regions";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        return result;
    }


    /**
     * 根据区域编号获取下级监控点列表
     */
    public static String getCamerasByRegionCode(String regionIndexCode, int pageNo, int pageSize) {

        String getRootApi = ARTEMIS_PATH + "/api/resource/v1/regions/regionIndexCode/cameras";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("http://", getRootApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", pageNo);
        jsonBody.put("pageSize", pageSize);
        jsonBody.put("regionIndexCode", regionIndexCode);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        return result;
    }

    /**
     * 根据设备编号获取监控点详细信息
     */
    public static String getCameraInfo(String cameraIndexCode) {

        String getRootApi = ARTEMIS_PATH + "/api/resource/v1/cameras/indexCode";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        return result;
    }

    /**
     * 查询监控点列表(此处为根据一定条件进行查询，若不指定查询条件，即全量获取所有的监控点信息)
     */
    public static String getCamerasByParams() {

        String getRootApi = ARTEMIS_PATH + "/api/resource/v1/camera/advance/cameraList";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 100);
//        jsonBody.put("regionIndexCode", "143c69e3-1478-4b31-8ec7-d0b78df09484");//区域唯一标识
//        jsonBody.put("cameraIndexCodes", "a8f74dcf14f846bb95c8dff6684f897e");//监控点唯一标识集,多个值使用英文逗号分隔
//        jsonBody.put("encodeDevIndexCode", "73c2e4903a4547f8812a26d329802cd0");//所属编码设备唯一标识
//        jsonBody.put("isCascade", 1);//是否级联,0：非级联 1：级联 2：不限
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        return result;
    }

    /**
     * 获取预览取流的url
     */
    public static String getCameraPreviewURL(String cameraIndexCode) {

        final String getCamsApi = ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getCamsApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);//监控点唯一标识
        jsonBody.put("streamType", 0);//码流类型,0:主码流，1:子码流，2:第三码流，参数不填，默认为主码流
        jsonBody.put("protocol", "rtsp");//取流协议,“rtsp”:RTSP协议,“rtmp”:RTMP协议,“hls”:HLS协议
        jsonBody.put("transmode", 1);//传输协议,0:UDP，1:TCP，默认是TCP
        jsonBody.put("expand", "streamform=ps");//扩展内容,格式：key=value
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        return result;
    }

    /**
     * 获取监控点回放取流url
     */
    public static String getCameraPlayBackURL(String cameraIndexCode, String beginTime, String endTime) {

        String getRootApi = ARTEMIS_PATH + "/api/video/v2/cameras/playbackURLs";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", cameraIndexCode);
        jsonBody.put("recordLocation", 1);//存储类型,0：中心存储，1：设备存储，默认为中心存储
        jsonBody.put("protocol", "rtsp");
        jsonBody.put("transmode", 1);
        jsonBody.put("beginTime", beginTime);//开始查询时间
        jsonBody.put("endTime", endTime);//结束查询时间,IOS8601格式：yyyy-MM-dd’T’HH:mm:ss.SSSXXX
        jsonBody.put("expand", "streamform=ps");
        String body = jsonBody.toJSONString();

        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        return result;
    }

    //TODO 布撤防接口
    public static String setDefenceStatus(String subSystemIndexCode, String deviceIndexCode, int status) {
        final String api = ARTEMIS_PATH + "/api/daf/v1/subsystem/status/set";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", api);
            }
        };
        String contentType = "application/json";
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subSystemIndexCode", subSystemIndexCode);
        jsonObject.put("status", status);//0-撤防，1-布防
        jsonArray.add(jsonObject);
        JSONObject params = new JSONObject();
        params.put("subSystems", jsonArray);
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("method", "SetDefenceStatus");
        jsonBody.put("params", params);
        String body = jsonBody.toJSONString();

        HashMap<String, String> header = new HashMap<>();
        header.put("DeviceIndexCode", deviceIndexCode);
        //System.out.println(jsonBody.toJSONString());
        String result = null;
        if (StringUtils.isNotEmpty(subSystemIndexCode) && StringUtils.isNotEmpty(deviceIndexCode)) {
            result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, header);
        }
        return result;
    }

    /**
     * 5.4.3.3	获取资源列表
     */
    public static String getResourceListByPage() {

        String getRootApi = ARTEMIS_PATH + "/api/irds/v1/deviceResource/resources";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 100);
        jsonBody.put("resourceType", "subSys");//子系统通道
        String body = jsonBody.toJSONString();
        //返回的indexCode是子系统编码()
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        return result;
    }

    /**
     * 监控点3D放大
     */
    public static String CameraThreeDSelZoom(){
        String getRootApi = ARTEMIS_PATH + "/api/video/v1/ptzs/selZoom";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", getRootApi);//根据现场环境部署确认是http还是https
            }
        };

        String contentType = "application/json";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("cameraIndexCode", "e66493f4ca9d4264a0bbb091887fec4b");
        jsonBody.put("startX", 10);
        jsonBody.put("startY", 40);
        jsonBody.put("endX", 10);
        jsonBody.put("endY", 40);
        String body = jsonBody.toJSONString();

        /**
         * STEP6：调用接口
         */
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
        System.out.println(result);
        return result;
    }

    /*
    根据监控点列表查询录像完整性结果
     */
    public static String list(List listRequest){
        String listDataApi = ARTEMIS_PATH +"/api/nms/v1/record/list";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",listDataApi);
            }
        };


        JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 20);
        String body= jsonBody.toJSONString(listRequest);
        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        return result;
    }

    /**
     * 时间格式转换
     * 将时间抓换成IOS8601格式
     * @param data
     * @param formatType
     * @return
     */
    public static String dateToString(Date data, String formatType){
        return new SimpleDateFormat(formatType).format(data);
    }
}
