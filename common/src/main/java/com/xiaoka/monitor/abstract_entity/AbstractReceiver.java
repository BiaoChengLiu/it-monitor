package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 发送者信息
 */
@Data
public abstract class AbstractReceiver extends BaseCacheEntity {
    /**
     * 告警发送类型
     */
    private String sendType;
    /**
     * qq消息推送地址
     */
    private String qqUrl;
    /**
     * wx消息推送地址
     */
    private String wxUrl;
    /**
     * 钉钉webHook地址
     */
    private String webHookUrl;
    /**
     * 邮箱发送者
     */
    private String emailUser;
    /**
     * 邮件发送密码或密钥
     */
    private String emailToken;
    /**
     * 邮件发送的服务器
     */
    private String emailHost;
    /**
     * 邮件发送的端口
     */
    private String emailPort;
    /**
     * 发送http地址
     */
    private String httpUrl;
    /**
     * 发送短信的接口
     */
    private String smsUrl;
    /**
     * 为各个接口提供一个token备用
     */
    private String token;
}
