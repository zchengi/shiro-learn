package com.cheng.cache;

import com.cheng.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * @author cheng
 *         2018/11/4 21:15
 */
@Component
public class RedisCache<K, V> implements Cache<K, V> {

    private final String CACHE_PREFIX = "cheng-cache:";

    @Resource
    private JedisUtil jedisUtil;

    @SuppressWarnings("unchecked")
    @Override
    public V get(K k) throws CacheException {

        System.out.println("从 redis 获取权限数据");

        byte[] value = jedisUtil.get(getKey(k));
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {

        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key, value);
        jedisUtil.expire(key, 600);

        return v;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(K k) throws CacheException {

        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.delete(key);

        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        // 此方法不需要重写
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    private byte[] getKey(K k) {

        if (k instanceof String) {
            return (CACHE_PREFIX + k).getBytes();
        }
        return SerializationUtils.serialize(k);
    }
}
