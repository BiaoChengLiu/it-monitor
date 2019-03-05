package com.xiaoka.monitor.abstract_entity;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public abstract class AbstractTeamMember extends BaseCacheEntity {

    /**
     * 团队ID
     */
    private String teamId;
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 工号
     */
    private String count;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * qq或群号
     */
    private String qqType;
    /**
     * QQ
     */
    private String qq;
    /**
     * wx或群号
     */
    private String wxType;
    /**
     * 微信号
     */
    private String wx;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 备注
     */
    private String remarks;
}
