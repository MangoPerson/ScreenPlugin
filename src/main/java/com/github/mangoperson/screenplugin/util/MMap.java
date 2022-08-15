package com.github.mangoperson.screenplugin.util;


import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class MMap<K, V> extends MList<Pair<K, V>> {
    public MMap() {
        super();
    }
    public MMap(MList<Pair<K, V>> list) {
        super(list);
    }
    public List<Pair<K, V>> asList() {
        return stream().collect(Collectors.toList());
    }
    public List<K> keyList() {
        return map((k, v) -> k);
    }
    public List<V> valueList() {
        return map((k, v) -> v);
    }
    public MList<Pair<K, V>> asMList() {
        return stream().collect(toMList());
    }
    public static <K, V> Collector<Pair<K, V>, ?, MMap<K, V>> toMMap() {
        return Collector.of(MMap::new, MMap::add, (t, l) -> {t.addAll(l); return t;});
    }
    public boolean add(K k, V v) {
        return super.add(new Pair<>(k, v));
    }
    public MMap<K, V> addR(K k, V v) {
        add(k, v);
        return this;
    }
    public void forEach(BiConsumer<? super K, V> action) {
        super.forEach(p -> action.accept(p.a, p.b));
    }
    public MMap<K, V> forEachM(BiConsumer<? super K, V> action) {
        super.forEach(p -> action.accept(p.a, p.b));
        return this;
    }
    public <R, S> MMap<R, S> biMap(BiFunction<? super K, ? super V, Pair<R, S>> function) {
        return super.map(p -> function.apply(p.a, p.b)).collect(toMMap());
    }
    public MMap<K, V> filter(BiPredicate<? super K, ? super V> predicate) {
        return super.filter(p -> predicate.test(p.a, p.b)).collect(toMMap());
    }
    public <R> MList<R> map(BiFunction<? super K, ? super V, ? extends R> function) {
        return super.map(p -> function.apply(p.a, p.b)).collect(toMList());
    }
    public Optional<Pair<K, V>> kmin(Comparator<? super K> comparator) {
        return super.min((t1, t2) -> comparator.compare(t1.a, t2.a));
    }
    public Optional<Pair<K, V>> vmin(Comparator<? super V> comparator) {
        return super.min((t1, t2) -> comparator.compare(t1.b, t2.b));
    }
    public Optional<Pair<K, V>> kmax(Comparator<? super K> comparator) {
        return super.max((t1, t2) -> comparator.compare(t1.a, t2.a));
    }
    public Optional<Pair<K, V>> vmax(Comparator<? super V> comparator) {
        return super.max((t1, t2) -> comparator.compare(t1.b, t2.b));
    }
    public boolean anyMatch(BiPredicate<? super K, ? super V> predicate) {
        return super.anyMatch(p -> predicate.test(p.a, p.b));
    }
    public boolean allMatch(BiPredicate<? super K, ? super V> predicate) {
        return super.allMatch(p -> predicate.test(p.a, p.b));
    }
    public boolean noneMatch(BiPredicate<? super K, ? super V> predicate) {
        return super.noneMatch(p -> predicate.test(p.a, p.b));
    }
    public void close() {
        super.close();
    }
}
