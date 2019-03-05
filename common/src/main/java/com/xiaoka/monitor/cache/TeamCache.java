package com.xiaoka.monitor.cache;

import com.xiaoka.monitor.abstract_entity.AbstractTeam;
import lombok.Data;

import java.util.List;

/**
 * 如果不是实体与缓存共同拥有的属性
 * 单独放在缓存中
 */
@Data
public class TeamCache extends AbstractTeam {

    /**
     * 关联的成员
     */
    private List<TeamMemberCache> memberList;
}
