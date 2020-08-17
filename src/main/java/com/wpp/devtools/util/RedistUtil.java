package com.wpp.devtools.util;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

/**
 * @program: volvo-sever
 * @description:
 * @author: wpp
 * @create: 2020-08-05
 **/
@Component
public class RedistUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 存放string类型到hash
     *
     * @param key1     key1
     * @param key2     key2
     * @param data    数据
     */
    public void setStringToHash(String key1,String key2, String data) {
        stringRedisTemplate.opsForHash().put(key1, key2, data);
    }

    /**
     * 获取放string类型到hash
     *
     * @param key1     key1
     * @param key2     key2
     */
    public boolean getStringToHash(String key1,String key2) {
       return stringRedisTemplate.opsForHash().hasKey(key1, key2);
    }

    /**
     * 存放string类型
     *
     * @param key     key
     * @param data    数据
     * @param timeout 超时间
     */
    public void setString(String key, String data, Long timeout) {
        try {
            stringRedisTemplate.opsForValue().set(key, data);
            if (timeout != null) {
                stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }

        } catch (Exception e) {

        }

    }

    /**
     * 开启Redis 事务
     *
     * @param
     */
    public void begin() {
        // 开启Redis 事务权限
        stringRedisTemplate.setEnableTransactionSupport(true);
        // 开启事务
        stringRedisTemplate.multi();

    }

    /**
     * 提交事务
     *
     * @param
     */
    public void exec() {
        // 成功提交事务
        stringRedisTemplate.exec();
    }

    /**
     * 回滚Redis 事务
     */
    public void discard() {
        stringRedisTemplate.discard();
    }

    /**
     * 存放string类型
     *
     * @param key  key
     * @param data 数据
     */
    public void setString(String key, String data) {
        setString(key, data, null);
    }

    /**
     * 根据key查询string类型
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    /**
     * 根据对应的key删除key
     *
     * @param key
     */
    public Boolean delKey(String key) {
        return stringRedisTemplate.delete(key);

    }

    /**
     * key + 1
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key,
                stringRedisTemplate.getConnectionFactory());
        return entityIdCounter.getAndIncrement();
    }

    public Long incr(String key, long seconds) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key,
                stringRedisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        if ((null == increment || increment.longValue() == 0) && seconds > 0) {//初始设置过期时间
            entityIdCounter.expire(seconds, TimeUnit.SECONDS);
        }
        return increment;
    }
}
