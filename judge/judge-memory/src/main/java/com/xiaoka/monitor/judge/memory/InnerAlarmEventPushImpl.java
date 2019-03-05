package com.xiaoka.monitor.judge.memory;


import com.xiaoka.monitor.cache.AlarmEventCache;
import com.xiaoka.monitor.judge.common.IAlarmEventPush;
import org.springframework.stereotype.Component;

@Component
public class InnerAlarmEventPushImpl implements IAlarmEventPush {

    @Override
    public void push(AlarmEventCache event) {

    }
}
