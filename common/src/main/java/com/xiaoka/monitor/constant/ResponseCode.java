package com.xiaoka.monitor.constant;

/**
 * 状态响应码
 */
public enum ResponseCode {
    OK(200, "成功"),
    ERROR_REQUEST(400, "错误请求"),
    NO_AUTH(401, "无权限"),
    NOT_FOUND(404, "不存在"),
    ERROR(500, "服务器异常");
    /**
     * 编码
     */
    private Integer code;
    /**
     * 编码对应的描述信息
     */
    private String desc;

    private ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
