package com.xiaoka.monitor.vo;

import com.xiaoka.monitor.constant.ResponseCode;
import lombok.Data;

/**
 * 响应数据封装
 */
@Data
public class Resp {
    /**
     * 响应码
     */
    private ResponseCode code;
    /**
     * 响应的数据
     */
    private Object data;

    public Resp() {

    }

    public Resp(ResponseCode code) {
        this.code = code;
    }

    /**
     * 是否ok
     *
     * @return
     */
    public boolean ok() {
        if (code == null) {
            return false;
        }
        return ResponseCode.OK.getCode().equals(code.getCode());
    }
}
