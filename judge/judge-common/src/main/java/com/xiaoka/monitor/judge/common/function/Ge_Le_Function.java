package com.xiaoka.monitor.judge.common.function;


import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.AlarmRuleCache;
import com.xiaoka.monitor.cache.GlobalAlarmRuleCache;

public class Ge_Le_Function extends RuleExpressionFunction {

    private Ge_Le_Function() {
    }

    private static class CondBetweenFunctionInner {
        public static final Ge_Le_Function instance = new Ge_Le_Function();
    }

    public static final Ge_Le_Function getInstance() {
        return CondBetweenFunctionInner.instance;
    }

    @Override
    public boolean trigger(Object realValObj, AbstractBaseAlarmRule rule) {

        Double confMinVal = null, confMaxVal = null, realVal = Double.parseDouble(realValObj.toString());
        if (rule instanceof GlobalAlarmRuleCache) {
            GlobalAlarmRuleCache gRule = (GlobalAlarmRuleCache) rule;
            confMinVal = gRule.getThresholdMin();
            confMaxVal = gRule.getThresholdMax();
        } else {
            AlarmRuleCache aRule = (AlarmRuleCache) rule;
            confMinVal = aRule.getThresholdMin();
            confMinVal = aRule.getThresholdMax();
        }
        return (realVal.compareTo(confMinVal) >= 0 ? true : false) && (realVal.compareTo(confMaxVal) <= 0 ? true : false);
    }

}
