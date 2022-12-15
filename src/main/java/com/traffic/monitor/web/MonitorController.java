package com.traffic.monitor.web;

import com.traffic.monitor.service.IMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author Wendy
 * @description 此实现方式：下载有录像功能设备的视频文件/录像（.mp4）
 */
@RestController
@Api(tags = "具有录像功能的设备接口")
public class MonitorController {

    @Value("${web.properity.file-path}")
    private String filePath;

    @Autowired
    private IMonitorService service;

//    //设置最多十个视频转码，可以设置大一些，随意的
//    public static CommandManager manager=new CommandManagerImpl(5);
//
//    @PostMapping("/getLiveStreamByIp")
//    @ApiOperation(value="windows 获取实时视频流",notes = "channelStream:101 代表通道一 主码流。102代表通道一 子码流；子码流比主码流小，但画质会有所下降 ，channelName是通道名，能保证唯一即可")
//    public ResultDTO getLiveStreamByIp(@RequestBody LiveDTO liveDTO, HttpServletRequest request, HttpServletResponse response) //throws  GlobalException
//    {
//
//        String liveUrl = "";
//        String channelName = liveDTO.getChannelName();
//        String ip = liveDTO.getIp();
//        String channelStream = liveDTO.getChannelStream();
//
//        //通过id查询这个任务
//        CommandTasker info = manager.query(channelName);
////       如果任务没存在，开启视频流
//        if(Objects.isNull(info)){
//            //执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
//            try {
//                String result= manager.start(channelName, "ffmpeg   -rtsp_transport tcp -i \"rtsp://"+ipcAccount+":"+ipcPassword+"@"+ip+"/Streaming/Channels/"+channelStream+"?transportmode=unicast\" -f flv -vcodec h264 -acodec aac -ar 44100  -s 1480*500 -crf 15 \"rtmp://localhost:1935/live/\""+channelName);
//                log.info("result"+result);
//            }catch (Exception e){
//                log.info("windows:"+e.getMessage());
//                throw new GlobalException(ErrorCodeConsts.ERROR,e.getMessage());
//            }
//        }else{
//            log.info(channelName+"任务已存在，从任务列表中取出数据返回");
//        }
//        // 如果是window rtmp版就返回 rtmp://localhost:1935/live/\""+channelName
//        liveUrl = "rtmp://"+currentserver+":1935/live/"+channelName;
//        // 下面这个是http-flv版的流
////        liveUrl= "/live?port=1935&app=myapp&stream="+channelName;
//
//        return ResultDTO.of(ResultEnum.SUCCESS).setData(liveUrl);
//    }

    /**
     * dvr back play video download by time video back play.
     *
     * 备注：如果下载下来的视频（.mp4）是0字节，说明该摄像设备没有录像功能。需要获取的是实时流视频。
     *
     * @param brandName
     * @param ip
     * @param port
     * @param account
     * @param password
     * @param starTime
     * @param endTime
     * @param routes
     * @param channel
     * @return
     */
    @PostMapping("/catch_video/by_time")
    @ApiOperation(value = "POST获取网络录像机影像", notes = "POST获取网络录像机影像，参数以 form-data 格式进行传输", httpMethod = "POST")
    public String fetchVideoByTime(
            @ApiParam(name = "brandName", value = "设备品牌名称", required = true) @RequestParam(value = "brandName") String brandName,
            @ApiParam(name = "ip", value = "设备IP地址", required = true) @RequestParam(value = "ip") String ip,
            @ApiParam(name = "port", value = "设备端口号", required = true) @RequestParam(value = "port") int port,
            @ApiParam(name = "account", value = "登录设备账号", required = true) @RequestParam(value = "account") String account,
            @ApiParam(name = "password", value = "登录设备密码", required = true) @RequestParam(value = "password") String password,
            @ApiParam(name = "starTime", value = "视频开始时间 (毫秒时间戳)", required = true) @RequestParam(value = "starTime") long starTime,
            @ApiParam(name = "endTime", value = "视频结束时间  (毫秒时间戳)", required = true) @RequestParam(value = "endTime") long endTime,
            @ApiParam(name = "routes", value = "硬盘机路数", required = true) @RequestParam(value = "routes") int routes,
            @ApiParam(name = "channel", value = "通道", required = true) @RequestParam(value = "channel") int channel) {
        if (brandName == null || brandName.isEmpty()) {
            return null;
        }
        switch (brandName) {
            case "HK":
                return service.fetchHKVideo(ip, port, account, password, starTime, endTime, routes, (short) channel);
            case "DH":
                return service.fetchDHVideo(ip, port, account, password, starTime, endTime, routes, (short) channel);
            default:
                return "BrandName  Error";
        }

    }

    /**
     * dvr catch picture return bytes.
     *
     * @param brandName
     * @param ip
     * @param port
     * @param routes
     * @param channel
     * @param account
     * @param password
     * @return
     */
    @PostMapping("/catch_picture")
    @ApiOperation(value = "POST获取摄像头抓图(响应 bate流)", notes = "POST获取摄像头抓图，参数以 form-data 格式进行传输", httpMethod = "POST")
    public byte[] fetchCatchPicture(
            @ApiParam(name = "brandName", value = "设备品牌名称", required = true) @RequestParam(value = "brandName") String brandName,
            @ApiParam(name = "ip", value = "设备IP地址", required = true) @RequestParam(value = "ip") String ip,
            @ApiParam(name = "port", value = "设备端口号", required = true) @RequestParam(value = "port") int port,
            @ApiParam(name = "routes", value = "硬盘机路数", required = true) @RequestParam(value = "routes") int routes,
            @ApiParam(name = "channel", value = "通道", required = true) @RequestParam(value = "channel") int channel,
            @ApiParam(name = "account", value = "登录设备账号", required = true) @RequestParam(value = "account") String account,
            @ApiParam(name = "password", value = "登录设备密码", required = true) @RequestParam(value = "password") String password) {
        try {
            FileInputStream in = getPictureStream(brandName, ip, port, routes, channel, account, password);
            byte[] bytes = new byte[in.available()];
            in.read(bytes, 0, in.available());
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * dvr catch picture return bytes.
     *
     * @param brandName
     * @param ip
     * @param port
     * @param routes
     * @param channel
     * @param account
     * @param password
     * @return
     */
    @PostMapping("/catch_picture_base64")
    @ApiOperation(value = "POST获取摄像头抓图(响应 Base64编码图片，前端使用 [data:img/jpeg;base64,] 接收)", notes = "POST获取摄像头抓图，参数以 form-data 格式进行传输", httpMethod = "POST")
    public String fetchCatchPictureByBase64(
            @ApiParam(name = "brandName", value = "设备品牌名称", required = true) @RequestParam(value = "brandName") String brandName,
            @ApiParam(name = "ip", value = "设备IP地址", required = true) @RequestParam(value = "ip") String ip,
            @ApiParam(name = "port", value = "设备端口号", required = true) @RequestParam(value = "port") int port,
            @ApiParam(name = "routes", value = "硬盘机路数", required = true) @RequestParam(value = "routes") int routes,
            @ApiParam(name = "channel", value = "通道", required = true) @RequestParam(value = "channel") int channel,
            @ApiParam(name = "account", value = "登录设备账号", required = true) @RequestParam(value = "account") String account,
            @ApiParam(name = "password", value = "登录设备密码", required = true) @RequestParam(value = "password") String password) {
        byte[] data = null;
        try {
            FileInputStream in = getPictureStream(brandName, ip, port, routes, channel, account, password);
            data = new byte[in.available()];
            in.read(data);
            in.close();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            // 返回Base64编码过的字节数组字符串
            return encoder.encode(data);
        } catch (Exception e) {
            return "brand or params error";
        }

    }

    private FileInputStream getPictureStream(String brandName, String ip, int port, int routes, int channel,
                                             String account, String password) {
        String fileName = null;
        FileInputStream in = null;

        try {
            if (brandName == null || brandName.isEmpty()) {
                return null;
            }
            if (brandName.equals("HK")) {
                fileName = service.fetchHKCatchPicture(ip, port, routes, channel, account, password);
            }

            if (brandName.equals("DH")) {
                fileName = service.fetchDHCatchPicture(ip, port, account, password, channel);
            }

            if (fileName == null || fileName.isEmpty()) {
                throw new RuntimeException("生成文件失败！");
            }
            File file = new File(fileName);
            in = new FileInputStream(file);
        } catch (Exception e) {

        }
        return in;
    }
}
