package com.xiaoka.monitor.judge.common.function;


import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.AlarmRuleCache;
import com.xiaoka.monitor.cache.GlobalAlarmRuleCache;

public class LeFunction extends RuleExpressionFunction {

    private LeFunction() {
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
        return realVal.compareTo(confVal) <= 0 ? true : false;

    }

    private static class CondLtFunctionInner {
        private static final LeFunction instance = new LeFunction();
    }

    public static final LeFunction getInstance() {
        return CondLtFunctionInner.instance;
    }


}
