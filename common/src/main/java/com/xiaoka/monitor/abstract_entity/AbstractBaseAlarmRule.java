package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractBaseAlarmRule extends BaseCacheEntity {
    /**
     * 指标名称
     */
    private String metric;
    /**
     * 大于、大于等于
     */
    private String condGt;
    /**
     * 小于、小于等于
     */
    private String condLt;
    /**
     * 等于
     */
    private String condEq;
    /**
     * 不等于
     */
    private String condNe;
    /**
     * 配置的最低阀值
     */
    private Double thresholdMin;
    /**
     * 配置的最高阀值
     */
    private Double thresholdMax;
    /**
     * 配置相等的阀值
     */
    private Double thresholdEq;
    /**
     * 配置的不相等的阀值
     */
    private Double thresholdNe;
    /**
     * 采样点数目,表示连续sample次如果都满足告警阀值，那么发送告警
     */
    private Integer sampleNum;
    /**
     * 告警间隔，2次告警发送的时间间隔
     */
    private Integer period;
    /**
     * 告警间隔单位
     */
    private Integer periodUnit;
    /**
     * 告警时间间隔，计算的以毫秒为单位的值
     */
    private Long periodMsec;
    /**
     * 告警级别 1：一般,2:警告,3:严重
     */
    private Integer level;
    /**
     * 规则条件
     */
    private String condType;
    /**
     * 配置的阀值的描述
     */
    private String confThresholdDesc;
}
