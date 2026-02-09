package com.amituofo.common.util;

import java.util.BitSet;
import java.util.Random;

public class BloomFilter {
    private BitSet bitSet;
    private int size;  // 位数组大小
    private int hashNum;  // 哈希函数个数
    private Random rand;

    public BloomFilter(int size, int hashNum) {
        this.bitSet = new BitSet(size);
        this.size = size;
        this.hashNum = hashNum;
        this.rand = new Random();
    }

    // 添加元素
    public void add(String key) {
        for (int i = 0; i < hashNum; i++) {
            int hash = getHash(key, i);
            bitSet.set(hash, true);
        }
    }

    // 判断元素是否存在
    public boolean contains(String key) {
        for (int i = 0; i < hashNum; i++) {
            int hash = getHash(key, i);
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }

    // 获取哈希值
    private int getHash(String key, int index) {
        int hash = 0;
        switch (index) {
            case 0:
                hash = Math.abs(key.hashCode());
                break;
            default:
                hash = Math.abs(key.hashCode() ^ rand.nextInt());
                break;
        }
        return hash % size;
    }
}