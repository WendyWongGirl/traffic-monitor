package com.traffic.monitor.test;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import com.traffic.monitor.vo.Dvr;
import com.traffic.monitor.vo.HCNetSDK;
import com.traffic.monitor.vo.Dvr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

/**
 * @author Wendy
 * @date 2022/12/6 10:20
 **/
class VideoDowload {

    private static Logger logger = LoggerFactory.getLogger(VideoDowload.class);
    private static HCNetSDK hcNetSDK = HCNetSDK.INSTANCE;
    private NativeLong userId;//用户句柄
    private NativeLong loadHandle;//下载句柄
    private Timer downloadTimer;

    /**
     * 按时间下载视频
     */
    private boolean downloadVideo(Dvr dvr, Date startTime, Date endTime, String filePath, int channel) {
        boolean initFlag = hcNetSDK.NET_DVR_Init();
        if (!initFlag) { //返回值为布尔值 fasle初始化失败
            logger.warn("hksdk(视频)-海康sdk初始化失败!");
            return false;
        }
        HCNetSDK.NET_DVR_DEVICEINFO_V30 deviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
//        userId = hcNetSDK.NET_DVR_Login_V30(dvr.getDvrip(), (short) dvr.getDvrport(), dvr.getDvrusername(), dvr.getDvrpassword(), deviceInfo);
        userId = hcNetSDK.NET_DVR_Login_V30("172.16.30.222", (short) 80, "admin", "DT-12345", deviceInfo);
        logger.info("hksdk(视频)-登录海康录像机信息,状态值:" + hcNetSDK.NET_DVR_GetLastError());
        long lUserId = userId.longValue();
        if (lUserId == -1) {
            logger.warn("hksdk(视频)-海康sdk登录失败!");
            return false;
        }
        loadHandle = new NativeLong(-1);
        if (loadHandle.intValue() == -1) {
            loadHandle = hcNetSDK.NET_DVR_GetFileByTime(userId, new NativeLong(channel), getHkTime(startTime), getHkTime(endTime), filePath);
            logger.info("hksdk(视频)-获取播放句柄信息,状态值:" + hcNetSDK.NET_DVR_GetLastError());
            if (loadHandle.intValue() >= 0) {
                // 判断文件夹是否存在
                File files = new File(filePath);
                if(!files.exists()){
                    files.mkdirs();
                }
                boolean downloadFlag = hcNetSDK.NET_DVR_PlayBackControl(loadHandle, hcNetSDK.NET_DVR_PLAYSTART, 0, null);
                int tmp = -1;
                IntByReference pos = new IntByReference();
                while (true) {
                    boolean backFlag = hcNetSDK.NET_DVR_PlayBackControl(loadHandle, hcNetSDK.NET_DVR_PLAYGETPOS, 0, pos);
                    if (!backFlag) {//防止单个线程死循环
                        return downloadFlag;
                    }
                    int produce = pos.getValue();
                    if ((produce % 10) == 0 && tmp != produce) {//输出进度
                        tmp = produce;
                        logger.info("hksdk(视频)-视频下载进度:" + "==" + produce + "%");
                    }
                    if (produce == 100) {//下载成功
                        hcNetSDK.NET_DVR_StopGetFile(loadHandle);
                        loadHandle.setValue(-1);
                        hcNetSDK.NET_DVR_Logout(userId);//退出录像机
                        logger.info("hksdk(视频)-退出状态" + hcNetSDK.NET_DVR_GetLastError());
                        //hcNetSDK.NET_DVR_Cleanup();
                        return true;
                    }
                    if (produce > 100) {//下载失败
                        hcNetSDK.NET_DVR_StopGetFile(loadHandle);
                        loadHandle.setValue(-1);
                        logger.warn("hksdk(视频)-海康sdk由于网络原因或DVR忙,下载异常终止!错误原因:" + hcNetSDK.NET_DVR_GetLastError());
                        //hcNetSDK.NET_DVR_Logout(userId);//退出录像机
                        //logger.info("hksdk(视频)-退出状态"+hcNetSDK.NET_DVR_GetLastError());
                        return false;
                    }
                }
            } else {
                System.out.println("hksdk(视频)-下载失败" + hcNetSDK.NET_DVR_GetLastError());
                return false;
            }
        }
        return false;
    }

    /**
     * 获取海康录像机格式的时间
     */
    private HCNetSDK.NET_DVR_TIME getHkTime(Date time) {
        HCNetSDK.NET_DVR_TIME structTime = new HCNetSDK.NET_DVR_TIME();
        String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);
        String[] times = str.split("-");
        structTime.dwYear = Integer.parseInt(times[0]);
        structTime.dwMonth = Integer.parseInt(times[1]);
        structTime.dwDay = Integer.parseInt(times[2]);
        structTime.dwHour = Integer.parseInt(times[3]);
        structTime.dwMinute = Integer.parseInt(times[4]);
        structTime.dwSecond = Integer.parseInt(times[5]);
        return structTime;
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = sdf.parse("20221206090000");   //开始时间
            endTime = sdf.parse("20221206110000");      //结束时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        VideoDowload test = new VideoDowload();
        Dvr dvr = new Dvr("172.16.30.222", (short)8000,"admin","DT-123456");
        int channel = 1;//通道
        System.out.print(test.downloadVideo(dvr, startTime, endTime, "D:\\testhk\\test.mp4", channel));
    }
}
