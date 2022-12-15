package com.traffic.monitor.web.hk;

import com.traffic.monitor.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：需配置 "ffmpeg"，否则报错：系统找不到指定的文件
 */
@RestController
@RequestMapping("/RtspApi")
@Api(tags = "使用ffemg插件截取rtsp视频流实现视频录制（海康相机）")
public class RtspToMP4Controller {
    @Autowired
    private RtspToMP4 rtspToMP4;

    private Map<Integer,Process> map=new HashMap<>();

    @ApiOperation(value = "开始录制")
    @GetMapping(value = "/videoRecordStart")
    public Result Start(Integer id, String FileName) {
        /*String ffmpegPath="D:\\work\\Test\\ffmpeg.exe";*/
        String ffmpegPath="ffmpeg";
//        String streamUrl="rtsp://admin:abcd1234@192.168.8.64:554/h264/ch1/main/av_stream";
        String streamUrl="rtsp://admin:DT-123456@172.16.30.222:554/h264/ch1/main/av_stream";
        /*String FilePath="D:\\work\\Test\\2022-01-06\\"+FileName;*/
//        String FilePath="/opt/videomp4/"+FileName;
        String FilePath ="G:\\JYGS\\downloadVideo\\"+FileName;
        Process process = rtspToMP4.StartRecord(ffmpegPath, streamUrl, FilePath);
        if(null!=process){
            map.put(id,process);
            return Result.ok(map);
        }
        return Result.error("开始录制错误{" + id +"}，请检查系统！");
    }

    @ApiOperation(value = "结束录制")
    @GetMapping(value = "/videoRecordStop")
    public Result stop(Integer id) {
        if(map.containsKey(id)){
            Process process = map.get(id);
            if(null!=process){
                rtspToMP4.stopRecord(process);
                return Result.ok(rtspToMP4);
            }
        }
        return Result.error("结束录制错误{" + id +"}，请检查系统！");
    }
}
