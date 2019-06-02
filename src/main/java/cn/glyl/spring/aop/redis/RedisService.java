package cn.glyl.spring.aop.redis;

import java.io.Serializable;
import java.util.List;

public interface RedisService {
    void set(String key, Object value, Long liveSeconds);

    void set(String key, Object value);

    Boolean setnx(String key, Object value);

    Boolean setnx(String key, Object value, Long liveSeconds);

    Object get(String key);

    void delete(String key);

    Long getExpire(String key);

    Boolean expire(String key, Long liveSeconds);

    Boolean hasKey(String key);

    void setToHash(String key, Object hashKey, Object value);

    Object getFromHash(String key, Object hashKey);

    Boolean hasHashKey(String key, Object hashKey);

    Long hashSize(String key);

    void deleteFromHash(String key, Object... hashKeys);

    Long increment(String key, Long value);

    Long increment(String key);

    void sendMessage(String channel, Serializable message);

    void setToList(String key, Object value);

    List<Object> getToList(String key);

    boolean releaseDistributedLock(String lockKey, String requestId);

}