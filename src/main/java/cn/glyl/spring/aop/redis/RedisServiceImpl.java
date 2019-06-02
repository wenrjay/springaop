package cn.glyl.spring.aop.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author donaldhan
 */
@Service
public class RedisServiceImpl implements RedisService {

    private final static String RELEASE_DISTRIBUTED_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(final String key, final Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Boolean setnx(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public Boolean setnx(String key, Object value, Long liveSeconds) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, liveSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(final String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void set(final String key, final Object value, final Long liveSeconds) {
        redisTemplate.opsForValue().set(key, value, liveSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public Boolean expire(String key, Long liveSeconds) {
        return redisTemplate.expire(key, liveSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void setToHash(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Object getFromHash(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Boolean hasHashKey(String key, Object hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public void deleteFromHash(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public Long increment(String key, Long value) {
        return redisTemplate.boundValueOps(key).increment(value);
    }

    @Override
    public Long increment(String key) {
        return redisTemplate.boundValueOps(key).increment(1L);
    }

    @Override
    public void sendMessage(String channel, Serializable message) {
        redisTemplate.convertAndSend(channel, message);
    }

    @Override
    public void setToList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public List<Object> getToList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey 锁
     * @param requestId 请求标识
     *
     * @return 是否释放成功
     */
    @Override
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(RELEASE_DISTRIBUTED_LOCK_SCRIPT);
        redisScript.setResultType(Long.class);
        Long execute = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);
        return Objects.equals(execute, 1L);
    }

}
