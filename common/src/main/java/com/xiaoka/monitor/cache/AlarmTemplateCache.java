package com.xiaoka.monitor.cache;

import com.xiaoka.monitor.abstract_entity.AbstractAlarmTemplate;
import lombok.Data;

import java.util.List;

/**
 * 如果不是实体与缓存共同拥有的属性
 * 单独放在缓存中
 */
@Data
public class AlarmTemplateCache extends AbstractAlarmTemplate {
    /**
     * 关联的告警规则
     */
    private List<AlarmRuleCache> ruleList;
}
