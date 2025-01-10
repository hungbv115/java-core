package com.learn.hungbv.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedCache<K, V> implements Cache<K, V>{
    private long ttlInMillis;  // Thời gian sống của mục cache (Time To Live)
    private int maxSize;       // Số lượng tối đa mục trong cache
    private final Map<K, CacheItem<V>> cache;

    public LinkedCache(int maxSize, long ttlInMillis) {
        this.ttlInMillis = ttlInMillis;
        this.maxSize = maxSize;
        // sử dụng LinkedHashMap để đảm bảo dữ liệu được truy cập gần nhất sẽ đứng ở đầu
        // LinkedHashMap là sự kết hợp giữa MapTable và LinkedList
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            // Override phương thức removeEldestEntry để tự động loại bỏ các mục cũ nhất
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheItem<V>> eldest) {
                return size() > maxSize;
            }
        };
    }

    // Đọc giá trị từ cache
    @Override
    public synchronized V get(K key) {
        CacheItem<V> cacheItem = cache.get(key);
        if (cacheItem == null || cacheItem.isExpired()) {
            return null;  // Nếu không có cache hoặc đã hết hạn
        }
        return cacheItem.getValue();
    }

    // Thêm một mục vào cache
    @Override
    public synchronized void put(K key, V value) {
        if(ttlInMillis == 0) {
            // Thêm vào cache mà không tính thời gian tồn tại
            cache.put(key, new CacheItem<>(value));
        } else {
            cache.put(key, new CacheItem<>(value, ttlInMillis));
        }
    }


    // Xóa mục khỏi cache
    @Override
    public synchronized void remove(K key) {
        cache.remove(key);
    }

    // Xóa hết tất cả các mục trong cache
    @Override
    public synchronized void clear() {
        cache.clear();
    }
}
