package com.traffic.monitor.web.hk;

import com.traffic.monitor.utils.ArtemisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class RtspToMP4 {
    private static final Logger log = LoggerFactory.getLogger(RtspToMP4.class);

    public class In implements Runnable{
        private InputStream inputStream;

        public In(InputStream inputStream) {
            this.inputStream = inputStream;
        }
        @Override
        public void run() {
            try {
                //转成字符输入流
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
                int len = -1;
                char[] c = new char[1024];
                //读取进程输入流中的内容
                while ((len = inputStreamReader.read(c)) != -1) {
                    String s = new String(c, 0, len);
                    System.out.print(s);
                }
            }catch (Exception e) {
                log.error("run:"+e);
            }
        }
    }

    /**
     * 开始录制
     * @param ffmpegPath ffmpeg路径
     * @param streamUrl 视频流
     * @param FilePath 保存的视频文件路径
     * @return
     */
    public Process StartRecord(String ffmpegPath,String streamUrl, String FilePath){
        ProcessBuilder processBuilder = new ProcessBuilder();
        //定义命令内容
        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        command.add("-rtsp_transport");
        command.add("tcp");
        command.add("-y");
        command.add("-i");
        command.add(streamUrl);
        command.add("-c");
        command.add("copy");
        command.add("-f");
        command.add("mp4");
        command.add(FilePath);
        processBuilder.command(command);
        System.out.println("脚本：" + command.toString());
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        try {
            //启动进程
            Process process = processBuilder.start();
            System.out.println("开始时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
            //获取输入流
            InputStream inputStream = process.getInputStream();
            Thread inThread = new Thread(new In(inputStream));
            inThread.start();
            return process;

        } catch (Exception e) {
            log.error("run:"+e);
        }
        return null;
    }

    /**
     * 停止录制
     * @param process
     * @return
     */
    public boolean stopRecord(Process process) {
        try {
            OutputStream os = process.getOutputStream();
            os.write("q".getBytes());
            // 一定要刷新
            os.flush();
            os.close();
        } catch (Exception err) {
            log.error("stopRecord:"+err);
            return false;
        }
        return true;
    }
}
