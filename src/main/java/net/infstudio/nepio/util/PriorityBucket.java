package net.infstudio.nepio.util;

import java.util.*;
import java.util.stream.Collectors;

public class PriorityBucket<K, V> {

    private Map<K, List<V>> buckets;

    public PriorityBucket(List<V> elements, Comparator<? super K> cmpK, Comparator<? super V> cmpV, Priority<K, V> priority) {
        buckets = new TreeMap<>(cmpK);
        elements = elements.stream().sorted(cmpV).collect(Collectors.toList());
        K prevPriority = null;
        for (V element : elements) {
            K curPriority = priority.getPriority(element);
            if (!curPriority.equals(prevPriority)) {
                buckets.put(curPriority, new ArrayList<>());
            }
            buckets.get(curPriority).add(element);
            prevPriority = curPriority;
        }
    }

    public Collection<K> getBuckets() {
        return buckets.keySet();
    }

    public List<V> getBucketContent(K key) {
        return buckets.get(key);
    }

    @FunctionalInterface
    public interface Priority<K, V> {
        K getPriority(V element);
    }

}
