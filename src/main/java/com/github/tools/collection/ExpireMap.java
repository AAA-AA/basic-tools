package com.github.tools.collection;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * this map has it's own expire
 * Created by renhongqiang on 2019-07-12 13:34
 */
@Slf4j
public class ExpireMap<K, V> extends ConcurrentHashMap<K, V> {

    private static long DEFAULT_EXPIRE_SECONDS = 2 * 60;

    private Timer cleanTimer;
    /**
     * record cached data
     */
    private Map<K, Long> expireDic = new ConcurrentHashMap<>();

    Map<K, Long> getExpireDic() {
        return this.expireDic;
    }

    public ExpireMap() {

    }

    public ExpireMap(int initialCapacity) {
        this(initialCapacity, 1, DEFAULT_EXPIRE_SECONDS);
    }

    public ExpireMap(long period, long expireSeconds) {
        this(1 << 4, period, expireSeconds);
    }

    public ExpireMap(int initialCapacity, long period, long expireSeconds) {
        super(initialCapacity);
        this.DEFAULT_EXPIRE_SECONDS = expireSeconds;
        cleanTimer = new Timer();
        cleanTimer.schedule(new CleanTask(this), 0, period);
    }

    @Override
    public synchronized V put(K key, V value) {
        expireDic.put(key, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(DEFAULT_EXPIRE_SECONDS));
        return super.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
        return !checkExpire(key) && super.containsKey(key);
    }

    public synchronized V put(K key, V value, long seconds) {
        expireDic.put(key, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
        return super.put(key, value);
    }

    public synchronized V put(K key, V value, long duration, TimeUnit unit) {
        expireDic.put(key, System.currentTimeMillis() + unit.convert(duration, unit));
        return super.put(key, value);
    }


    public boolean isEmpty() {
        return entrySet().size() == 0;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            return Boolean.FALSE;
        }
        Set<Entry<K, V>> set = super.entrySet();
        Iterator<Entry<K, V>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (value.equals(entry.getValue())) {
                if (checkExpire(entry.getKey())) {
                    iterator.remove();
                    return Boolean.FALSE;
                } else return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;

    }

    public Collection<V> values() {
        Collection<V> values = super.values();
        if (values == null || values.size() < 1) return values;
        Iterator<V> iterator = values.iterator();
        while (iterator.hasNext()) {
            V next = iterator.next();
            if (!containsValue(next)) iterator.remove();
        }
        return values;
    }

    public int size() {
        return entrySet().size();
    }


    public V get(Object key) {
        if (key == null) {
            return null;
        }
        if (checkExpire(key)) {
            return null;
        }
        return super.get(key);
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();
        Set<Entry<K,V>> set = super.entrySet();
        for (Entry<K, V> kvEntry : set) {
            if (!checkExpire(kvEntry.getKey())) {
                entries.add(kvEntry);
            }
        }
        return entries;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet())
            expireDic.put(e.getKey(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(DEFAULT_EXPIRE_SECONDS));
        super.putAll(m);
    }

    private static class CleanTask<K, V> extends TimerTask {

        private ExpireMap<K, V> expireMap;

        CleanTask(ExpireMap<K, V> expireMap) {
            this.expireMap = expireMap;
        }

        @Override
        public void run() {
            if (expireMap.isEmpty()) {
                return;
            }
            Set<Entry<K, V>> entries = expireMap.entrySet();
            Iterator<Entry<K, V>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> next = iterator.next();
                boolean expire = expireMap.checkExpire(next.getKey());
                if (expire) {
                    System.out.println("key: " + next.getKey() + " is expired: , to now: " + (expireMap.getExpireDic().get(next.getKey()) - System.currentTimeMillis()));
                    iterator.remove();
                    expireMap.getExpireDic().remove(next.getKey());
                }
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        expireDic.clear();
        cleanTimer.cancel();
    }

    private synchronized boolean checkExpire(Object key) {
        if (!expireDic.containsKey(key)) {
            return Boolean.FALSE;
        }
        long expiryTime = expireDic.get(key);
        return System.currentTimeMillis() > expiryTime;
    }

    public static void main(String[] args) throws InterruptedException {
        ExpireMap<String, String> map = new ExpireMap<>(1, 10);
        map.put("aaa", "test", 3, TimeUnit.SECONDS);
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        for (int i = 1; i < 100; i++) {
            executorService.submit(() -> {
                putData(map);
            });
        }
        for (int i = 0; i < 60; i++) {
            Thread.sleep(1000);
            printMap(map);
        }
    }

    private static void putData(ExpireMap<String, String> map) {
        for (int i = 0; i < 1000; i++) {
            map.put("test" + (System.currentTimeMillis() + i), "jfdsaj" + UUID.randomUUID().toString());
        }
    }

    private static void printMap(ExpireMap<String, String> map) {
        log.info(">>>>>>>>>>>>>>>>map size:{}<<<<<<<<<<<<<<<<<<<<<", map.size());
    }
}
