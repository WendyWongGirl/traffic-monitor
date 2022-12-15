package com.traffic.monitor.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.traffic.monitor.utils.ArtemisUtils;
import com.traffic.monitor.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Wendy
 * @description 此实现方式：批量读取监控点列表中的摄像设备。适用于服务区有多个摄像设备的情况。
 * 适用场景：与海康应用平台接口对接。需要和海康应用平台接口对接吗？如果需要的话，就要在应用平台安装“API网关”组件，这个是在运行管理中心中查看是否已安装，若是尚未安装，需联系现场海康人员进行安装
 */
@Slf4j
@RestController
@RequestMapping("/t-sys-video")
@Api(tags = "海康视频接口-监控列表读取")
public class VideoController extends BaseController {

    @ApiOperation("获取实时预览取流的url")
    @PostMapping("/cameraIndexCode")
    public Result cameraIndexCode() {

        //查看监控点列表
        String camerasByParams = ArtemisUtils.getCamerasByParams();
        JSONObject jsonObject = JSONObject.parseObject(camerasByParams);

        String data = jsonObject.getString("data");

        JSONObject jsonData = JSONObject.parseObject(data);
        String list = jsonData.getString("list");
        //JSONObject jsonList = JSONObject.parseObject();
        JSONArray jsonArray = JSONObject.parseArray(list);
        ArrayList<String> result = new ArrayList<>();
        if (Objects.nonNull(jsonArray) && jsonArray.size() > 0) {
            for (Object object : jsonArray) {
                JSONObject parseObject = JSONObject.parseObject(object.toString());
                String cameraIndexCode = parseObject.getString("cameraIndexCode");
                result.add(cameraIndexCode);
            }
        }
        for (String cameraIndexCode : result) {

            String cameraPreviewURL = null;
            try {
                //获取实时预览URL
                cameraPreviewURL = ArtemisUtils.getCameraPreviewURL(cameraIndexCode);
                JSONObject json = JSONObject.parseObject(cameraPreviewURL);
                String resultData = json.getString("data");
                JSONObject da = JSONObject.parseObject(resultData);
                String url = da.getString("url");
                return Result.ok(url);
            } catch (Exception e) {
                log.error(e.getMessage());
                return Result.error("获取实时预览异常，请检查系统！");
            }

        }
        return Result.ok();
    }



    /**
     * 根据每个侯问室的摄像头编号记录审问期间视频
     *
     * @param cameraIndexCode
     * @param beginTime
     * @param endTime
     * @return
     */
    @ApiOperation("获取监控点回放取流url")
    @PostMapping("/indexTime")
    public Result indexTime(@RequestParam("cameraIndexCode") @ApiParam(name = "cameraIndexCode", value = "摄像头id") String cameraIndexCode,
                                  @RequestParam("beginTime") @ApiParam(name = "beginTime", value = "开始时间") String beginTime,
                                  @RequestParam("endTime") @ApiParam(name = "endTime", value = "结束时间") String endTime) {

        String cameraPlayBackURL = null;
        try {
            cameraPlayBackURL = ArtemisUtils.getCameraPlayBackURL(cameraIndexCode, beginTime, endTime);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("获取监控点回放取流异常，请检查系统！");
        }

        JSONObject json = JSONObject.parseObject(cameraPlayBackURL);
        String resultData = json.getString("data");
        JSONObject da = JSONObject.parseObject(resultData);
        String url = da.getString("url");
        return Result.ok(url);

    }
}