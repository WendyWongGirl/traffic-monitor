//package com.traffic.monitor.test;
//
//public class TestRTSP {
//
//    public static void main(String[] args) {
//
//    }
//
//    public static void test(){
//        //必须要加入这两个东西
//        System.load("G:\\JYGS\\opencv\\build\\x64\\vc14\\bin\\opencv_world3415.dll");
//        System.load("G:\\JYGS\\opencv\\build\\bin\\opencv_ffmpeg3415_64.dll");
//        VideoCapture vc = new VideoCapture();
//        boolean isOpen = vc.open("rtsp://admin:jdrx1234567@192.168.60.30:554/h264/ch1/sub/av_stream");
//
//        System.out.println("isOpen="+isOpen);
//
//        Mat mat = new Mat();
//        String winName = "showFrame";
//        int height = 600,width = 800;
//        HighGui.namedWindow(winName);
//        HighGui.resizeWindow(winName, width, height);
//
//        while(vc.read(mat)){
//            System.out.println("read.......");
//            // 重置大小
//            Mat dst = new Mat();
//            Imgproc.resize(mat, dst, new Size(width,height));
//            // 显示
//            HighGui.imshow(winName, dst);
//            // waitkey 必须要，否则无法显示
//            int key = HighGui.waitKey(1);
//            System.out.println("key="+key);
//            //esc键退出
//            if(key == 27){
//                break;
//            }
//            //拿到了每帧之后要干嘛就是后面逻辑的事情了
//        }
//        HighGui.destroyAllWindows();
//        vc.release();
//    }
//
//}
