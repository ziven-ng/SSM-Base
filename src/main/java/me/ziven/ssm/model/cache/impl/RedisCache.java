package me.ziven.ssm.model.cache.impl;

import me.ziven.ssm.common.kit.ProtoStuffKit;
import me.ziven.ssm.model.cache.Cache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * redis缓存
 */
@Component("RedisCache")
public class RedisCache implements Cache {

    private final Logger LOG = LogManager.getLogger(this.getClass());

    private RedisTemplate<String, String> redisTemplate;

    private static boolean ENABLE = true;

    @Autowired
    public RedisCache(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public <T> boolean put(String key, T obj) {
        if (!ENABLE) return false;
        try {
            final byte[] bytesKey = key.getBytes();
            final byte[] bytesValue = ProtoStuffKit.serialize(obj);
            return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.setNX(bytesKey, bytesValue));
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);
        }
        return false;
    }

    @Override
    public <T> void putWithExpire(String key, T obj, int expireTime) {
        if (!ENABLE) return;
        try {
            final byte[] bytesKey = key.getBytes();
            final byte[] bytesValue = ProtoStuffKit.serialize(obj);
            put(bytesKey, bytesValue, expireTime);
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);
        }
    }


    @Override
    public <T> boolean putList(String key, List<T> objList) {
        if (!ENABLE) return false;
        try {
            final byte[] bytesKey = key.getBytes();
            final byte[] bytesValue = ProtoStuffKit.serializeList(objList);
            return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.setNX(bytesKey, bytesValue));
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);

        }
        return false;
    }

    @Override
    public <T> boolean putListWithExpire(String key, List<T> objList, int expireTime) {
        if (!ENABLE) return false;
        try {
            final byte[] bytesKey = key.getBytes();
            final byte[] bytesValue = ProtoStuffKit.serializeList(objList);
            return put(bytesKey, bytesValue, expireTime);
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);

        }
        return false;
    }

    private boolean put(byte[] bytesKey, byte[] bytesValue, int expireTime) {
        try {
            return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                connection.setEx(bytesKey, expireTime, bytesValue);
                return true;
            });
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);

            return false;
        }
    }

    @Override
    public <T> T get(String key, Class<T> targetClass) {
        if (!ENABLE) return null;
        try {
            byte[] result = redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key.getBytes()));
            if (result == null) {
                return null;
            }
            return ProtoStuffKit.deserialize(result, targetClass);
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);

        }
        return null;
    }

    @Override
    public <T> List<T> getList(String key, Class<T> targetClass) {
        if (!ENABLE) return null;
        try {
            byte[] result = redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key.getBytes()));
            if (result == null) {
                return null;
            }
            return ProtoStuffKit.deserializeList(result, targetClass);
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);

        }
        return null;
    }

    @Override
    public void delete(String key) {
        if (!ENABLE) return;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);

        }
    }

    @Override
    public void deleteByPattern(String pattern) {
        if (!ENABLE) return;
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            redisTemplate.delete(keys);
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);
        }

    }

    @Override
    public void clear() {
        if (!ENABLE) return;
        try {
            deleteByPattern(CACHE_NAME + SEPARATOR + "*");
        } catch (Exception e) {
            LOG.error("Cache Error:" + e.getMessage(),e);
        }
    }
}
