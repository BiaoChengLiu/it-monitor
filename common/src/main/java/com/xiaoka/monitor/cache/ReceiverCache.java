package com.xiaoka.monitor.cache;

import com.xiaoka.monitor.abstract_entity.AbstractReceiver;
import lombok.Data;

/**
 * 如果不是实体与缓存共同拥有的属性
 * 单独放在缓存中
 */
@Data
public class ReceiverCache extends AbstractReceiver {
}
