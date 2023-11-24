package ru.clevertec.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {
//    private Cache<Long, Card> cache;
//
//    @BeforeEach
//    void init() {
//        cache = new LFUCache<>(3);
//
//        cache.put(1L, new Card("1"));
//        cache.put(2L, new Card("2"));
//        cache.put(3L, new Card("3"));
//    }
//
//    @Test
//    void testGetWithExistingKey() {
//        var expected = new Card("1");
//
//        var actual = cache.get(1L);
//
//        assertNotNull(actual);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testGetWithNotExistingKey() {
//        var actual = cache.get(0L);
//
//        assertNull(actual);
//    }
//
//    @Test
//    void testPutWithNewValue() {
//        var expected = new Card("4");
//        cache.put(4L, expected);
//
//        assertNull(cache.get(1L));
//        assertEquals(expected, cache.get(4L));
//    }
//
//    @Test
//    void testRemoveByExistingKey() {
//        var expected = new Card("1");
//
//        var actual = cache.remove(1L);
//
//        assertNotNull(actual);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void testRemoveByNotExistingKey() {
//        var actual = cache.remove(0L);
//
//        assertNull(actual);
//    }
}