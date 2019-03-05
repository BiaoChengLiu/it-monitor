package com.xiaoka.monitor.judge.common;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ustcinfo.ishare.eip.si.cache.common.ICache;
import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.*;
import com.xiaoka.monitor.judge.common.function.RuleExpressionFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 具体判断agent指标的实现
 */
public class JudgeHanlderRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AgentMetric agentMetric;
    private ICache cache;
    private IAlarmEventPush alarmEventPush;

    public JudgeHanlderRunnable(AgentMetric agentMetric, ICache cache, IAlarmEventPush alarmEventPush) {
        this.agentMetric = agentMetric;
        this.cache = cache;
        this.alarmEventPush = alarmEventPush;
    }

    @Override
    public void run() {
        //关联的主机
        HostCache hostCache = cache.get(HostCache.class, agentMetric.getHostId());
        if (hostCache == null) {
            logger.error("告警判定失败,未找到关联的主机信息,hostId={},type={},confId={}", agentMetric.getHostId(), agentMetric.getType(), agentMetric.getConfId());
            return;
        }
        /**
         * ----思路
         * 1.全局告警规则的优先级<集群告警规则优先级<具体组件(主机)关联的告警规则优先级
         * 2.全局告警规则 与 集群告警规则 或 全局告警规则 与 具体组件监控告警规则 具有覆盖、后者覆盖前者
         */
        MonitorAgentCache monitorAgent = cache.get(MonitorAgentCache.class, agentMetric.getConfId());
        if (monitorAgent == null) {
            logger.error("监控组件不存在,如果持续上发数据，请在服务器上删除它");
            return;
        }
        String clusterId = monitorAgent.getClusterId();
        ClusterCache cluster = cache.get(ClusterCache.class, clusterId);
        if (cluster == null) {
            logger.error("监控组件关联的集群不存在,如果持续上发数据，请在服务器上删除它");
            return;
        }
        /**
         *key
         * --- 指标名称
         * value
         * --- 对应的告警规则
         */
        Map<String, AlarmRuleItem> ruleItemMap = Maps.newHashMap();

        //全局告警规则
        List<GlobalAlarmRuleCache> globalRuleList = cache.getAll(GlobalAlarmRuleCache.class);
        if (globalRuleList != null) {
            for (GlobalAlarmRuleCache globalRule : globalRuleList) {
                AlarmRuleItem item = new AlarmRuleItem();
                item.setType(AlarmRuleItem.TYPE.GLOBAL);
                item.setGlobalRuleId(globalRule.getId());
                ruleItemMap.put(globalRule.getMetric(), item);
            }
        }
        // 集群告警模板、集群告警规则
        String clusterTemplateId = cluster.getTemplateId();
        AlarmTemplateCache clusterAlarmTemplate = cache.get(AlarmTemplateCache.class, clusterTemplateId);
        if (clusterAlarmTemplate != null) {
            List<AlarmRuleCache> clusterRuleList = clusterAlarmTemplate.getRuleList();
            if (clusterRuleList != null) {
                for (AlarmRuleCache clusterRule : clusterRuleList) {
                    AlarmRuleItem item = new AlarmRuleItem();
                    item.setType(AlarmRuleItem.TYPE.CLUSTER);
                    item.setClusterRuleId(clusterRule.getId());
                    ruleItemMap.put(clusterRule.getMetric(), item);
                }
            }
        }
        // 组件告警模板、组件告警规则
        String agentTemplateId = monitorAgent.getTemplateId();
        AlarmTemplateCache agentAlarmTemplate = cache.get(AlarmTemplateCache.class, agentTemplateId);
        if (agentAlarmTemplate != null) {
            List<AlarmRuleCache> agentRuleList = agentAlarmTemplate.getRuleList();
            if (agentRuleList != null) {
                for (AlarmRuleCache agentRule : agentRuleList) {
                    AlarmRuleItem item = new AlarmRuleItem();
                    item.setType(AlarmRuleItem.TYPE.AGENT);
                    item.setAgentRuleId(agentRule.getId());
                    ruleItemMap.put(agentRule.getMetric(), item);
                }
            }
        }
        // 上发数据转为map格式
        Map<String, Object> metricMap = agentMetric.getMetricMap();
        /**
         * key
         * ---teamId-receiverId： 唯一确定收告警的对象
         * value
         * ---告警内容
         */
        Map<String, StringBuffer> alarmEventMap = Maps.newHashMap();
        /**
         * 触发告警的指标
         */
        Set<String> triggerMetricSet = Sets.newHashSet();
        /**
         * 规则判断
         */
        Set<Map.Entry<String, AlarmRuleItem>> ruleItemSet = ruleItemMap.entrySet();
        for (Map.Entry<String, AlarmRuleItem> itemEntry : ruleItemSet) {
            String metric = itemEntry.getKey();
            Object realVal = metricMap.get(metric);
            if (realVal == null) {
                continue;
            }
            // 仅处理数值
            if (!(realVal instanceof Float || realVal instanceof Integer || realVal instanceof Double || realVal instanceof Long)) {
                logger.info("非数值指标不处理:{}={}", metric, realVal);
                continue;
            }
            AlarmRuleItem item = itemEntry.getValue();
            AbstractBaseAlarmRule rule = null;
            String teamId = null, receiverId = null, templateContent = null, templateName = null;
            if (item.getType() == AlarmRuleItem.TYPE.GLOBAL) {
                GlobalAlarmRuleCache gRule = cache.get(GlobalAlarmRuleCache.class, item.getGlobalRuleId());
                teamId = gRule.getTeamId();
                receiverId = gRule.getReceiverId();
                templateContent = gRule.getTemplate();
                templateName = gRule.getName();
                rule = gRule;
            } else if (item.getType() == AlarmRuleItem.TYPE.CLUSTER) {
                AlarmRuleCache cRule = cache.get(AlarmRuleCache.class, item.getClusterRuleId());
                teamId = clusterAlarmTemplate.getTeamId();
                receiverId = clusterAlarmTemplate.getReceiverId();
                templateContent = clusterAlarmTemplate.getTemplate();
                templateName = clusterAlarmTemplate.getName();
                rule = cRule;
            } else if (item.getType() == AlarmRuleItem.TYPE.AGENT) {
                AlarmRuleCache aRule = cache.get(AlarmRuleCache.class, item.getAgentRuleId());
                teamId = agentAlarmTemplate.getTeamId();
                receiverId = agentAlarmTemplate.getReceiverId();
                templateContent = agentAlarmTemplate.getTemplate();
                templateName = agentAlarmTemplate.getName();
                rule = aRule;
            }
            // 用到的变量都在这里先获取
            /**
             * 先判断告警的时间间隔是否处于有效期内<br/>
             * 在告警时间间隔有效期内的指标不用继续判定了
             */
            AlarmMetricStateCache alarmMetricState = cache.get(AlarmMetricStateCache.class, agentMetric.getConfId() + metric);
            if (alarmMetricState != null) {
                if (agentMetric.getTimestamp() - alarmMetricState.getPreTimestamp() <= rule.getPeriodMsec()) {
                    // 告警处于有效期内，此次告警放弃
                    continue;
                }
            }
            RuleExpressionFunction fun = RuleExpressionFunction.getFunction(rule);
            if (!fun.trigger(realVal, rule)) {
                // 没有触发告警规则，后面也就不用判断了
                continue;
            }
            /**
             * 走到这里说明这个点不仅触发了阀值，而且告警时间间隔已经超出了有效期<br/>
             * 可以判断配置的所有采样点是否都满足阀值<br/>
             * 如果采样点为0那么就不用判断历史记录了
             */
            if (rule.getSampleNum() != null && rule.getSampleNum().intValue() > 0) {
                List<AgentMetric> hList = cache.lrange(agentMetric.getClass(), rule.getSampleNum() - 1);
                if (hList == null || hList.size() < rule.getSampleNum() - 1) {
                    // 未能够达到要求的采样点数、放弃
                    continue;
                }
                boolean trigerAll = true;
                for (AgentMetric h : hList) {
                    final Object hRealVal = h.getMetricMap().get(metric);
                    if (!fun.trigger(hRealVal, rule)) {
                        // 触发
                        trigerAll = false;
                        break;
                    }
                }
                if (!trigerAll) {
                    // 未达到规定采样点数符合规则、放弃
                    continue;
                }
            }
            triggerMetricSet.add(metric);
            //这个应该产生了告警事件了
            String eventKey = teamId + "-" + receiverId;
            StringBuffer alarmBody = alarmEventMap.get(eventKey);
            if (alarmBody == null) {
                alarmBody = new StringBuffer("");
                alarmEventMap.put(eventKey, alarmBody);
            }

            //合并告警信息

            String level = rule.getLevel() == null ? "" : rule.getLevel() + "";

            templateContent = templateContent.replaceAll("\\{level\\}", rule.getLevel() + "");
            templateContent = templateContent.replaceAll("\\{ip\\}", hostCache.getIpList().toString());
            templateContent = templateContent.replaceAll("\\{hostname\\}", hostCache.getName());
            templateContent = templateContent.replaceAll("\\{target\\}", agentMetric.getType());
            templateContent = templateContent.replaceAll("\\{merit\\}", rule.getMetric());
            templateContent = templateContent.replaceAll("\\{confThreshold\\}", rule.getConfThresholdDesc());
            templateContent = templateContent.replaceAll("\\{realThreshold\\}", realVal.toString());
            templateContent = templateContent.replaceAll("\\{sample\\}", rule.getSampleNum() + "");
            templateContent = templateContent.replaceAll("\\{periodTime\\}", rule.getPeriod() + "");
            templateContent = templateContent.replaceAll("\\{triggerTime\\}", agentMetric.getTimestamp() + "");
            templateContent = templateContent.replaceAll("\\{templateName\\}", templateName);

            alarmBody.append(templateContent).append("\n");
        }

        //生成告警事件

        Set<Map.Entry<String, StringBuffer>> alarmEventSet = alarmEventMap.entrySet();
        for (Map.Entry<String, StringBuffer> m : alarmEventSet) {
            // 还原数据
            String[] arr = m.getKey().split("-");
            String teamId = arr[0];
            String receiverId = arr[1];
            AlarmEventCache event = new AlarmEventCache();
            event.setConfId(monitorAgent.getId());
            event.setTeamId(teamId);
            event.setReceiverId(receiverId);
            event.setContent(m.getValue().toString());
            // 告警事件推送、等待sender来发送
            alarmEventPush.push(event);
        }
        // 触发了的告警指标的状态需要修改
        for (String metricName : triggerMetricSet) {
            AlarmMetricStateCache state = new AlarmMetricStateCache();
            state.setId(agentMetric.getConfId() + metricName);
            state.setPreTimestamp(System.currentTimeMillis());
            cache.put(state);
        }
    }

}
