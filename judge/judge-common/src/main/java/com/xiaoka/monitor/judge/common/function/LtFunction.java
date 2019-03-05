package com.xiaoka.monitor.judge.common.function;


import com.xiaoka.monitor.abstract_entity.AbstractBaseAlarmRule;
import com.xiaoka.monitor.cache.AlarmRuleCache;
import com.xiaoka.monitor.cache.GlobalAlarmRuleCache;

public class LtFunction extends RuleExpressionFunction {

    private LtFunction() {
    }

    @Override
    public boolean trigger(Object realValObj, AbstractBaseAlarmRule rule) {
        Double confVal = null, realVal = Double.parseDouble(realValObj.toString());
        if (rule instanceof GlobalAlarmRuleCache) {
            GlobalAlarmRuleCache gRule = (GlobalAlarmRuleCache) rule;
            confVal = gRule.getThresholdMax();
        } else {
            AlarmRuleCache aRule = (AlarmRuleCache) rule;
            confVal = aRule.getThresholdMax();
        }
        return realVal.compareTo(confVal) < 0 ? true : false;
    }

    private static class CondLtFunctionInner {
        private static final LtFunction instance = new LtFunction();
    }

    public static final LtFunction getInstance() {
        return CondLtFunctionInner.instance;
    }


}
