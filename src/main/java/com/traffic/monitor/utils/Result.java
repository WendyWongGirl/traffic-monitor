package com.traffic.monitor.utils;

import com.traffic.monitor.exception.IErrorCode;
import com.traffic.monitor.exception.SysCode;

import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public Result() {
        put("code", SysCode.S0.getCode());
        put("msg", SysCode.S0.getMessage());
    }

    public static Result error() {
        return error(SysCode.E500);
    }

    public static Result operate(boolean b){
        if(b){
            return Result.ok();
        }
        return Result.error();
    }

    public static Result error(String msg) {
        return error(SysCode.E500);
    }

    public static Result error(IErrorCode errorCode) {
        Result r = new Result();
        r.put("code", errorCode.getCode());
        r.put("msg", errorCode.getMessage());
        return r;
    }

    public static Result error(String code ,String msg) {
        Result r = new Result();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put("msg", msg);
        return r;
    }
    public static Result ok(Object obj) {
        Result r = new Result();
        r.put("data", obj);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    public static Result error401() {
        return error(SysCode.E401);
    }

    public static Result error403() {
        return error(SysCode.E403);
    }

    public static Result data(Object data){
        return Result.ok().put("data",data);
    }

    public static Result page(Object page){
        return Result.ok().put("page",page);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
