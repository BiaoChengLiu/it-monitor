package com.xiaoka.monitor.cache;

import com.ustcinfo.ishare.eip.si.cache.common.BaseCacheEntity;
import lombok.Data;

/**
 * 将实体与缓存共同拥有的属性放入抽象类中
 */
@Data
public class AlarmEventCache extends BaseCacheEntity {

    /**
     * 组件ID
     */
    private String confId;
    /**
     * 发送给哪个团队
     */
    private String teamId;
    /**
     * 告警接收者
     */
    private String receiverId;
    /**
     * 具体发送的内容
     */
    private String content;

}
