package ru.clevertec.cache.factory;


import ru.clevertec.cache.Cache;
import ru.clevertec.cache.impl.LFUCache;
import ru.clevertec.cache.impl.LRUCache;

public class CacheFactory<K, V> {
    private String algorithm;

    private int size;

    public Cache<K, V> create() {
        switch (algorithm) {
            case "LFU" -> new LFUCache<>(size);
            case "LRU" -> new LRUCache<>(size);
            default -> throw new RuntimeException();
        }
        throw new RuntimeException("Wrong cache algorithm.");
    }
}