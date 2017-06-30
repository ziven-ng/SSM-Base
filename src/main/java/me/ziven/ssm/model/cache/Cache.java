package me.ziven.ssm.model.cache;

import java.util.List;

/**
 * Created by ziven on 2017/6/29.
 */
public interface Cache {

    String SEPARATOR = "|";
    String CACHE_NAME = "cache";//缓存名
    int CAHCE_EXPIRE = 60;//默认缓存时间

    <T> boolean put(String key, T obj);

    <T> void putWithExpire(String key, T obj, final int expireTime);

    <T> boolean putList(String key, List<T> objList);

    <T> boolean putListWithExpire(String key, List<T> objList, final int expireTime);

    <T> T get(final String key, Class<T> targetClass);

    <T> List<T> getList(final String key, Class<T> targetClass);

    void delete(String key);

    void deleteByPattern(String pattern);

    void clear();
}
