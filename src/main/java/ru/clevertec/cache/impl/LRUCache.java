package ru.clevertec.cache.impl;

import ru.clevertec.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {
    private static final float HASH_LOAD_FACTOR = 0.75f;
    private final LinkedHashMap<K, V> linkedHashMap;
    private final int size;

    public LRUCache(int size) {
        this.size = size;
        int capacity = (int) Math.ceil(size / HASH_LOAD_FACTOR) + 1;
        linkedHashMap = new LinkedHashMap<K, V>(capacity, HASH_LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > LRUCache.this.size;
            }
        };
    }

    public synchronized V get(K key) {
        return linkedHashMap.get(key);
    }

    public void put(K key, V value) {
        linkedHashMap.put(key, value);
    }

    @Override
    public V remove(K key) {
        return linkedHashMap.remove(key);
    }
}
