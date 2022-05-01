package com.github.tools.collection;

import com.github.tools.pub.Checks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static <E> List<E> asList(List<E> ...elements) {
        List<E> list = new ArrayList<>();
        for (List<E> element : elements) {
            list.addAll(element);
        }
        return list;
    }


    public static <K,V> Map<K,V> asMap(K k, V val) {
        HashMap<K,V> map = new HashMap<>();
        map.put(k, val);
        return map;
    }

    public static <E> CollectionAgg<E> join(Collection<E> left, Collection<E> right) {
        CollectionAgg agg = new CollectionAgg();
        if (left == null || left.isEmpty()) {
            agg.setRightPart(right);
            agg.setUnionPart(right);
            return agg;
        }
        if (right == null || right.isEmpty()) {
            agg.setLeftPart(left);
            agg.setUnionPart(left);
            return agg;
        }
        Collection<E> common = new HashSet<>();
        for (E leftEle : left) {
            for (E rightEle : right) {
                if (leftEle.equals(rightEle)) {
                    common.add(leftEle);
                }
            }
        }
        agg.setCommonPart(common);
        Collection<E> leftPart = left.stream().filter(e -> !common.contains(e)).collect(Collectors.toSet());
        Collection<E> rightPart = right.stream().filter(e -> !common.contains(e)).collect(Collectors.toSet());

        agg.setLeftPart(leftPart);
        agg.setRightPart(rightPart);
        agg.setUnionPart(getUnion(leftPart, rightPart, common));
        return agg;
    }

    private static <E> Collection<E> getUnion(Collection<E> leftPart, Collection<E> rightPart, Collection<E> common) {
        Collection<E> union = new HashSet<>();
        if (leftPart != null && !leftPart.isEmpty()) {
            union.addAll(leftPart);
        }
        if (rightPart != null && !rightPart.isEmpty()) {
            union.addAll(rightPart);
        }
        if (common != null && !common.isEmpty()) {
            union.addAll(common);
        }
        return union;
    }

    public static class CollectionAgg<E> {
        private Collection<E> leftPart;//补集左边部分
        private Collection<E> rightPart;//补集右边部分
        private Collection<E> commonPart;//交集
        private Collection<E> unionPart;//并集

        public Collection<E> getLeftPart() {
            return leftPart;
        }

        public void setLeftPart(Collection<E> leftPart) {
            this.leftPart = leftPart;
        }

        public Collection<E> getRightPart() {
            return rightPart;
        }

        public void setRightPart(Collection<E> rightPart) {
            this.rightPart = rightPart;
        }

        public Collection<E> getCommonPart() {
            return commonPart;
        }

        public void setCommonPart(Collection<E> commonPart) {
            this.commonPart = commonPart;
        }

        public Collection<E> getUnionPart() {
            return unionPart;
        }

        public void setUnionPart(Collection<E> unionPart) {
            this.unionPart = unionPart;
        }
    }
}

