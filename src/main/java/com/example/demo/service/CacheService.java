package com.example.demo.service;

import org.springframework.cache.CacheManager;

public class CacheService {

  private final String cacheName;
  private final CacheManager cacheManager;

  public CacheService(final String cacheName, final CacheManager cacheManager) {
    this.cacheName = cacheName;
    this.cacheManager = cacheManager;
  }

  public <T> void put(T data, String key) {
    cacheManager.getCache(cacheName).putIfAbsent(key, data);
  }

  public <T> T get(final String id, Class<T> clazz) {
    return cacheManager.getCache(cacheName).get(id, clazz);
  }

  public void cleanCache() {
    cacheManager.getCache(cacheName).clear();
  }

}
