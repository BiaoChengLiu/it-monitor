package com.xiaoka.monitor.judge.common.function;


import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.AlarmRuleCache;
import com.xiaoka.monitor.cache.GlobalAlarmRuleCache;

/**
 * 计算merit>=XXX格式的表达式
 *
 * @author liuchengbiao
 */
public class GtFunction extends RuleExpressionFunction {

    private GtFunction() {
    }

    @Override
    public boolean trigger(Object realValObj, AbstractBaseAlarmRule rule) {
        Double confVal = null, realVal = Double.parseDouble(realValObj.toString());
        if (rule instanceof GlobalAlarmRuleCache) {
            GlobalAlarmRuleCache gRule = (GlobalAlarmRuleCache) rule;
            confVal = gRule.getThresholdMin();
        } else {
            AlarmRuleCache aRule = (AlarmRuleCache) rule;
            confVal = aRule.getThresholdMin();
        }
        return realVal.compareTo(confVal) > 0 ? true : false;
    }

    private static class CondGtFunctionInner {
        private static final GtFunction instance = new GtFunction();
    }

    public static final GtFunction getInstance() {
        return CondGtFunctionInner.instance;
    }


}
