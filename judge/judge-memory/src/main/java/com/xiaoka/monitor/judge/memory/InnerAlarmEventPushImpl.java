package com.xiaoka.monitor.judge.memory;


import com.xiaoka.monitor.MonitorContext;
import com.xiaoka.monitor.cache.AlarmEventCache;
import com.xiaoka.monitor.judge.common.IAlarmEventPush;
import org.springframework.stereotype.Component;

@Component
public class InnerAlarmEventPushImpl implements IAlarmEventPush {

    @Override
    public void push(AlarmEventCache event) {
        /**
         * 添加到队列中
         */
        MonitorContext.WAIT_SENDER_ALARM_QUEUE.add(event);
    }
}
