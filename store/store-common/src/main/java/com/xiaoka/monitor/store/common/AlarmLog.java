package com.xiaoka.monitor.store.common;

import lombok.Data;

import java.util.Date;

/**
 * 告警日志
 */
@Data
public class AlarmLog {
    /**
     * 被监控的组件
     */
    private String confId;
    /**
     * 告警发送的项目组
     */
    private String teamId;
    /**
     * 告警接收者或发送身份信息
     */
    private String receiverId;
    /**
     * 告警内容
     */
    private String content;
    /**
     * 告警发送状态
     */
    private Integer status;
    /**
     * 发送失败异常信息
     */
    private String errorStack;
    /**
     * 发送时间
     */
    private Date sendTime;
}
