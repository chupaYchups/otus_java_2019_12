package ru.chupaYchups.cachehw;

import java.util.List;
import java.util.Map;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private Map<K, V> cacheMap;
    private List<HwListener<K, V>> listenerList;

    public MyCache(Map<K, V> cacheMap, List<HwListener<K, V>> listenerList) {
        this.cacheMap = cacheMap;
        this.listenerList = listenerList;
    }

    @Override
    public void put(K key, V value) {
        cacheMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        cacheMap.remove(key);
    }

    @Override
    public V get(K key) {
        return cacheMap.get(key);
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
