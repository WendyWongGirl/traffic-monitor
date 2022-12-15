package com.traffic.monitor.test;

import com.sun.jna.Native;

import java.io.File;

public class AllTest {
    public static void main(String[] args) {
//        int i  = 1;
//        i = i++;
//        System.out.println(i);
//        System.out.println(++i);

        File lib = new File("lib/" + System.mapLibraryName("HCNetSDK"));
        System.out.println(lib.getAbsolutePath());
    }
}
