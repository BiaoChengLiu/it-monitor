package com.xiaoka.monitor.constant;

/**
 * 告警规则条件
 */
public interface RuleCondType {
    /**
     * 等于
     */
    public static final String EQ = "EQ";
    /**
     * 大于
     */
    public static final String GT = "GT";
    /**
     * 大于等于
     */
    public static final String GE = "GE";
    /**
     * 小于
     */
    public static final String LT = "LT";
    /**
     * 小于等于
     */
    public static final String LE = "LE";
    /**
     * 不等于
     */
    public static final String NE = "NE";
    /**
     * 大于等于 且 小于等于
     */
    public static final String GE_LE = "GE_LE";
    /**
     * 大于等于 且 小于
     */
    public static final String GE_LT = "GE_LT";
    /**
     * 大于 且 小于等于
     */
    public static final String GT_LE = "GT_LE";
    /**
     * 大于 且 小于
     */
    public static final String GT_LT = "GT_LT";
}
