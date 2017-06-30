package me.ziven.ssm.model.cache.impl;

import me.ziven.ssm.common.kit.ProtoStuffKit;
import me.ziven.ssm.model.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * redis缓存
 * <p>
 * 采用Jedis Cluster
 */
//@Component("RedisClusterCache")
public class RedisClusterCache implements Cache {

    @Autowired
    private JedisCluster jedisCluster;


    @Override
    public <T> boolean put(String key, T obj) {
        final byte[] bytesKey = key.getBytes();
        final byte[] bytesValue = ProtoStuffKit.serialize(obj);
        return null != jedisCluster.set(bytesKey, bytesValue);
    }

    @Override
    public <T> void putWithExpire(String key, T obj, int expireTime) {
        final byte[] bytesKey = key.getBytes();
        final byte[] bytesValue = ProtoStuffKit.serialize(obj);
        jedisCluster.setex(bytesKey, expireTime, bytesValue);
    }


    @Override
    public <T> boolean putList(String key, List<T> objList) {
        final byte[] bytesKey = key.getBytes();
        final byte[] bytesValue = ProtoStuffKit.serializeList(objList);
        return null != jedisCluster.set(bytesKey, bytesValue);
    }

    @Override
    public <T> boolean putListWithExpire(String key, List<T> objList, int expireTime) {
        final byte[] bytesKey = key.getBytes();
        final byte[] bytesValue = ProtoStuffKit.serializeList(objList);
        return null != jedisCluster.setex(bytesKey, expireTime, bytesValue);
    }


    @Override
    public <T> T get(String key, Class<T> targetClass) {
        byte[] result = jedisCluster.get(key.getBytes());
        if (result == null) {
            return null;
        }
        return ProtoStuffKit.deserialize(result, targetClass);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> targetClass) {
        byte[] result = jedisCluster.get(key.getBytes());
        if (result == null) {
            return null;
        }
        return ProtoStuffKit.deserializeList(result, targetClass);
    }

    @Override
    public void delete(String key) {
        jedisCluster.del(key);
    }

    @Override
    public void deleteByPattern(String pattern) {
        Set<String> keys = this.keys(pattern);
        for (String key : keys) {
            jedisCluster.del(key);
        }
    }

    @Override
    public void clear() {
        deleteByPattern(CACHE_NAME + SEPARATOR + "*");
    }


    private Set<String> keys(String pattern) {
        Set<String> keys = new HashSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(pattern));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //用完一定要close这个链接！！！
                connection.close();
            }
        }
        return keys;
    }
}
