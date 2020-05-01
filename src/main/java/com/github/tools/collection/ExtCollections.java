package com.github.tools.collection;

import com.github.tools.pub.Checks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class ExtCollections {
    public ExtCollections() {
    }

    public static <K, V, E> Map<K, V> kv(Function<E, K> kf, Function<E, V> vf, List<E> beans) {
        Map<K, V> fieldbean = new HashMap();
        if (Checks.isEmpty(beans)) {
            return fieldbean;
        } else {
            Iterator iterator = beans.iterator();
            while (iterator.hasNext()) {
                E bean = (E) iterator.next();
                K key = kf.apply(bean);
                V value = vf.apply(bean);
                fieldbean.put(key, value);
            }
            return fieldbean;
        }
    }

    public static <K, V> Map<K, V> kvFieldBean(Function<V, K> function, List<V> beans) {
        Map<K, V> fieldbean = new HashMap();
        if (Checks.isEmpty(beans)) {
            return fieldbean;
        } else {
            Iterator iterator = beans.iterator();
            while (iterator.hasNext()) {
                V bean = (V) iterator.next();
                K key = function.apply(bean);
                fieldbean.put(key, bean);
            }
            return fieldbean;
        }
    }

    public static <K, V> Map<K, List<V>> kvFieldBeans(Function<V, K> function, List<V> beans) {
        Map<K, List<V>> fieldbeans = new HashMap();
        if (Checks.isEmpty(beans)) {
            return fieldbeans;
        } else {
            Object bean;
            Object values;
            for (Iterator iterator = beans.iterator(); iterator.hasNext(); ((List) values).add(bean)) {
                bean = iterator.next();
                K key = function.apply((V) bean);
                values = (List) fieldbeans.get(key);
                if (null == values) {
                    values = new ArrayList();
                    fieldbeans.put(key, (List<V>) values);
                }
            }
            return fieldbeans;
        }
    }

    public static BigDecimal scale2(BigDecimal price) {
        return null == price ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : price.setScale(2, RoundingMode.HALF_UP);
    }
}

