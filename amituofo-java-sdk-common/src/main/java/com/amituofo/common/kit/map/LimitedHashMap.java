package com.amituofo.common.kit.map;

import java.util.LinkedHashMap;
import java.util.Map;

public class LimitedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LimitedHashMap(int maxSize) {
        super((int)(maxSize / 0.75f) + 1, 0.75f, true); // accessOrder=true → LRU
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;  // 达到上限时移除最老的
    }
}