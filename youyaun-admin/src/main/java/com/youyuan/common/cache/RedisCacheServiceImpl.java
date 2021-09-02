package com.youyuan.common.cache;

import com.alibaba.fastjson.JSON;
import com.youyuan.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
*
* @author yizhong.liao
* @createTime 2021/9/2 13:12
*/
@Slf4j
@Service("defaultCacheService")
public class RedisCacheServiceImpl implements CacheService{

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Cache getCache(String name) {
        if (StringUtil.isEmpty(name)) {
            log.warn("RedisCacheServiceImpl.getCache warn: name is Empty");
            return null;
        }
        return cacheManager.getCache(name);
    }

    @Override
    public Collection<String> getCacheName() {
        return cacheManager.getCacheNames();
    }

    @Override
    public void put(String cacheName, Object key, Object value) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            log.warn("RedisCacheServiceImpl.put warn: cache[" + cacheName + "] is null, can not put data in cache");
            return;
        }
        if (key == null) {
            log.warn("RedisCacheServiceImpl.put warn: key is null, can not put data in cache");
            return;
        }
        String valueStr = value instanceof String ? value.toString() : JSON.toJSONString(value);
        cache.put(key.toString(), valueStr);
    }

    @Override
    public <T> T get(String cacheName, Object key, Class<T> type) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            log.warn("RedisCacheServiceImpl.get warn: cache[" + cacheName + "] is null, can not get data from cache");
            return null;
        }
        log.debug("RedisCacheServiceImpl.get debug: request for key [" + key.toString() + "] in cache [" + cacheName
                + "], Object type is [" + type.getSimpleName() + "]");
        String value = cache.get(key, String.class);
        if (type == String.class) {
            return (T) value;
        } else {
            return JSON.parseObject(value, type);
        }
    }

    @Override
    public <T> List<T> getList(String cacheName, Object key, Class<T> type) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            log.warn("RedisCacheServiceImpl.get warn: cache[" + cacheName + "] is null, can not get data from cache");
            return null;
        }
        log.debug("RedisCacheServiceImpl.get, request for key [" + key.toString() + "] in cache [" + cacheName
                + "], Object type is [" + type.getSimpleName() + "]");
        String value = cache.get(key, String.class);
        return JSON.parseArray(value, type);

    }

    @Override
    public void evict(String cacheName, Object key) {
        Cache cache = getCache(cacheName);
        if (cache == null) {
            log.warn("RedisCacheServiceImpl.put warn: cache[" + cacheName + "] is null, can not evict data from cache");
            return;
        }
        cache.evict(key);
    }

    @Override
    public void clear(String cacheName) {
        Cache cache = getCache(cacheName);
        if (null == cache) {
            log.warn("RedisCacheServiceImpl.put warn: cache[" + cacheName + "] is null, can not clear all data in cache");
            return;
        }
        cache.clear();
    }

    public CacheManager getCacheManager(){
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
