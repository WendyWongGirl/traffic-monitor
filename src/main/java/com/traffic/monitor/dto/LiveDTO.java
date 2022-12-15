package com.traffic.monitor.dto;

import io.swagger.annotations.ApiModelProperty;


/**
 * 查询条件
 *
 */
public class LiveDTO {
    @ApiModelProperty(name = "channelName", value = "channelName", required = true, dataType = "String")
    public String channelName;

    @ApiModelProperty(name = "channelStream", value = "channelStream", required = true, dataType = "String")
    public String channelStream;

    @ApiModelProperty(name = "ip", value = "ip", required = false, dataType = "String")
    public String ip;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelStream() {
        return channelStream;
    }

    public void setChannelStream(String channelStream) {
        this.channelStream = channelStream;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
