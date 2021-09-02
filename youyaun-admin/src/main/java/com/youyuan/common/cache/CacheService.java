package com.youyuan.common.cache;

import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.List;

/**
* 缓存接口
* @author yizhong.liao
* @createTime 2021/9/2 13:05
*/
public interface CacheService {

    /**
     * 根据名称获取缓存
     * @param name
     * @return
     */
    Cache getCache(String name);

    /**
     * 获取所有的缓存
     * @return
     */
    Collection<String> getCacheName();

    /**
     * 保存任何类型的数据
     * @param cacheName
     * @param key
     * @param Value
     */
    void put(String cacheName, Object key, Object Value);

    /**
     *
     * @param cacheName
     * @param key
     * @param type
     * @param <T>
     * @return
     */
    <T> T get(String cacheName, Object key, Class<T> type);

    <T> List<T> getList(String cacheName, Object key, Class<T> type);

    void evict(String cacheName, Object key);

    /**
     * 根据缓存名称清除缓存
     * @param cacheName
     */
    void clear(String cacheName);
}
