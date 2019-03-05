package com.xiaoka.monitor.judge.common.function;


import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.AlarmRuleCache;
import com.xiaoka.monitor.cache.GlobalAlarmRuleCache;
import com.xiaoka.monitor.constant.RuleCondType;

/**
 * 表达式函数
 *
 * @author liuchengbiao
 */
public abstract class RuleExpressionFunction {

    /**
     * 返回计算函数
     *
     * @param rule
     * @return
     */
    public static RuleExpressionFunction getFunction(AbstractBaseAlarmRule rule) {
        String condType = null;
        if (rule instanceof GlobalAlarmRuleCache) {
            GlobalAlarmRuleCache gRule = (GlobalAlarmRuleCache) rule;
            condType = gRule.getCondType();
        } else if (rule instanceof AlarmRuleCache) {
            AlarmRuleCache aRule = (AlarmRuleCache) rule;
            condType = aRule.getCondType();
        }
        if (condType == null) {
            return null;
        }
        if (RuleCondType.EQ.equals(condType)) {
            return EqFunction.getInstance();
        } else if (RuleCondType.GT.equals(condType)) {
            return GtFunction.getInstance();
        } else if (RuleCondType.GE.equals(condType)) {
            return GeFunction.getInstance();
        } else if (RuleCondType.LT.equals(condType)) {
            return LtFunction.getInstance();
        } else if (RuleCondType.LE.equals(condType)) {
            return LeFunction.getInstance();
        } else if (RuleCondType.NE.equals(condType)) {
            return NeFunction.getInstance();
        } else if (RuleCondType.GE_LE.equals(condType)) {
            return Ge_Le_Function.getInstance();
        } else if (RuleCondType.GE_LT.equals(condType)) {
            return Ge_Lt_Function.getInstance();
        } else if (RuleCondType.GT_LE.equals(condType)) {
            return Gt_Le_Function.getInstance();
        } else if (RuleCondType.GT_LT.equals(condType)) {
            return Gt_Lt_Function.getInstance();
        }
        return null;
    }

    /**
     * 计算函数具体的实现
     *
     * @param realVal 实际的值
     * @param rule    计算的规则
     * @return
     */
    public abstract boolean trigger(Object realVal, AbstractBaseAlarmRule rule);
}
