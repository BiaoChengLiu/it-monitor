package com.xiaoka.monitor.judge.common.function;


import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.AlarmRuleCache;
import com.xiaoka.monitor.cache.GlobalAlarmRuleCache;

public class EqFunction extends RuleExpressionFunction {

    private EqFunction() {
    }

    @Override
    public boolean trigger(Object realVal, AbstractBaseAlarmRule rule) {
        if (rule instanceof GlobalAlarmRuleCache) {
            GlobalAlarmRuleCache gRule = (GlobalAlarmRuleCache) rule;
            return gRule.getThresholdEq().equals(realVal);
        }
        AlarmRuleCache aRule = (AlarmRuleCache) rule;
        return aRule.getThresholdEq().equals(realVal);
    }

    private static class CondLtFunctionInner {
        private static final EqFunction instance = new EqFunction();
    }

    public static final EqFunction getInstance() {
        return CondLtFunctionInner.instance;
    }


}
