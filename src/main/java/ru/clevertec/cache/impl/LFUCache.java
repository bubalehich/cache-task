package ru.clevertec.cache.impl;

import ru.clevertec.cache.Cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class LFUCache<K, V> implements Cache<K, V> {
    private final int size;
    private final TreeMap<Integer, Set<K>> frequencyKeys;
    private final Map<K, V> cache;
    private final Map<K, Integer> frequencies;

    public LFUCache(int size) {
        this.size = size;
        frequencyKeys = new TreeMap<>();
        cache = new HashMap<>();
        frequencies = new HashMap<>();
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        if (value != null) {
            incrementFrequency(key);
        }

        return value;
    }

    @Override
    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            incrementFrequency(key);
            cache.put(key, value);
        } else {
            if (size == cache.size()) {
                removeLeastFrequentEntry();
            }
            addNewEntry(key, value);
        }
    }

    @Override
    public V remove(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }

        int frequency = frequencies.get(key);
        frequencies.remove(key);
        frequencyKeys.get(frequency).remove(key);

        return cache.remove(key);
    }

    private void addNewEntry(K key, V value) {
        cache.put(key, value);
        setKeyFrequency(key, 1);
    }

    private void incrementFrequency(K key) {
        int frequency = frequencies.get(key);
        Set<K> keys = frequencyKeys.get(frequency);
        keys.remove(key);
        if (keys.isEmpty()) {
            frequencyKeys.remove(frequency);
        }
        setKeyFrequency(key, frequency + 1);
    }

    private void setKeyFrequency(K key, int frequency) {
        frequencies.put(key, frequency);
        frequencyKeys.putIfAbsent(frequency, new LinkedHashSet<>());
        frequencyKeys.get(frequency).add(key);
    }

    private void removeLeastFrequentEntry() {
        Map.Entry<Integer, Set<K>> entry = frequencyKeys.firstEntry();
        Integer frequency = entry.getKey();
        Set<K> keys = entry.getValue();
        K keyToEvict = keys.iterator().next();
        keys.remove(keyToEvict);
        if (keys.isEmpty()) {
            frequencyKeys.remove(frequency);
        }
        frequencies.remove(keyToEvict);
        cache.remove(keyToEvict);
    }
}
