package com.xiaoka.monitor.common.abstract_entity;

import lombok.Data;

/**
 * 主机抽象类
 */
@Data
public class AbstractHost {

    /**
     * 主机名称
     */
    private String name;
    /**
     * 主机ssh端口
     */
    private Integer port;
    /**
     * 主机python bin 路径
     */
    private String pathBin;
}
