package com.xiaoka.monitor.judge.memory;

import com.ustcinfo.ishare.eip.si.cache.common.ICache;
import com.xiaoka.monitor.MonitorContext;
import com.xiaoka.monitor.cache.AgentMetric;
import com.xiaoka.monitor.judge.common.IAlarmEventPush;
import com.xiaoka.monitor.judge.common.JudgeHanlderRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * judge组件
 */
@Component
public class JudgeComponent {

    @Autowired
    private ICache cache;
    @Autowired
    private IAlarmEventPush alarmEventPush;

    /**
     * 初始化bean的时候执行
     * 定时获取队列中的指标信息
     * 然后使用多线程解析指标信息
     */
    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 10;
                while (true) {
                    try {
                        AgentMetric agentMetric = MonitorContext.AGENT_METRIC_QUEUE.poll(5, TimeUnit.SECONDS);
                        if (agentMetric == null) {
                            Thread.sleep(time);
                            continue;
                        }
                        MonitorContext.AGENT_METRIC_JUDGE_POOL.execute(new JudgeHanlderRunnable(agentMetric, cache, alarmEventPush));
                    } catch (Exception e) {
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
