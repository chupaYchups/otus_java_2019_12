package ru.chupaYchups.cachehw;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sergey
 * created on 14.12.18.
 */
@Component
public class MyCache<K, V> implements HwCache<K, V> {

    public static final String PUT = "PUT";
    public static final String REMOVE = "REMOVE";
    public static final String GET = "GET";

    private Map<K, V> cacheMap;
    private List<HwListener<K, V>> listenerList;

    public MyCache() {
        this.cacheMap = new HashMap<>();
        this.listenerList = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cacheMap.put(key, value);
        listenerList.forEach(kvHwListener -> kvHwListener.notify(key, value, PUT));
    }

    @Override
    public void remove(K key) {
        V value = cacheMap.remove(key);
        listenerList.forEach(kvHwListener -> kvHwListener.notify(key, value, REMOVE));
    }

    @Override
    public V get(K key) {
        V value = cacheMap.get(key);
        listenerList.forEach(kvHwListener -> kvHwListener.notify(key, value, GET));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listenerList.remove(listener);
    }
}
