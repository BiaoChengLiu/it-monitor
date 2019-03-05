package com.xiaoka.monitor.judge.common.function;


import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.AlarmRuleCache;
import com.xiaoka.monitor.cache.GlobalAlarmRuleCache;

public class NeFunction extends RuleExpressionFunction {

    private NeFunction() {
    }

    @Override
    public boolean trigger(Object realValObj, AbstractBaseAlarmRule rule) {

        Double confVal = null, realVal = Double.parseDouble(realValObj.toString());
        if (rule instanceof GlobalAlarmRuleCache) {
            GlobalAlarmRuleCache gRule = (GlobalAlarmRuleCache) rule;
            confVal = gRule.getThresholdEq();
        } else {
            AlarmRuleCache aRule = (AlarmRuleCache) rule;
            confVal = aRule.getThresholdEq();
        }
        return !realVal.equals(confVal);
    }

    private static class CondLtFunctionInner {
        private static final NeFunction instance = new NeFunction();
    }

    public static final NeFunction getInstance() {
        return CondLtFunctionInner.instance;
    }


}
