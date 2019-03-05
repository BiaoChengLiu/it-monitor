package com.xiaoka.monitor.judge.common;

import lombok.Data;

/**
 * 用于继承、覆盖告警规则使用
 */
@Data
public class AlarmRuleItem {
    /**
     * 类型：全局、集群、组件
     */
    private TYPE type;
    /**
     * 全局告警规则ID
     */
    private String globalRuleId;
    /**
     * 集群告警规则ID
     */
    private String clusterRuleId;
    /**
     * 组件告警规则ID
     */
    private String agentRuleId;


    /**
     * 告警类型
     */
    static enum TYPE {
        GLOBAL, CLUSTER, AGENT;
    }
}
