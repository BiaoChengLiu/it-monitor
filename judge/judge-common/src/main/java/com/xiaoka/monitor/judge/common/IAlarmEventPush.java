package com.xiaoka.monitor.judge.common;


import com.xiaoka.monitor.cache.AlarmEventCache;

/**
 * 告警事件推送
 */
public interface IAlarmEventPush {

    /**
     * 告警事件推送
     *
     * @param event
     */
    void push(AlarmEventCache event);
}
