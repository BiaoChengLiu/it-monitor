package com.xiaoka.monitor.cache;

import com.xiaoka.monitor.abstract_entity.AbstractHost;
import lombok.Data;

import java.util.List;

/**
 * 如果不是实体与缓存共同拥有的属性
 * 单独放在缓存中
 */
@Data
public class HostCache extends AbstractHost {
    /**
     * 关联的用户
     */
    private List<UserCache> userList;
    /**
     * 关联的IP
     */
    private List<IPCache> ipList;
    /**
     * 关联的集群
     */
    private List<ClusterCache> clusterList;

}
