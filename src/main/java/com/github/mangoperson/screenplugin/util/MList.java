package com.github.mangoperson.screenplugin.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class MList<E> extends ArrayList<E> {
    public MList() {
        super();
    }
    @SafeVarargs
    public MList(E... elements) {
        new MList<E>(Arrays.stream(elements).toList());
    }
    public MList(List<E> list) {
        super(list);
    }
    public MList<E> addR(E e) {
        MList<E> m = this;
        m.add(e);
        return m;
    }
    public MList<E> removeR(E e) {
        MList<E> m = this;
        m.remove(e);
        return m;
    }
    public MList<E> removeR(int index) {
        MList<E> m = this;
        m.remove(index);
        return m;
    }

    public static <T> Collector<T, ?, MList<T>> toMList() {
        return Collector.of(MList::new, MList::add, (t, l) -> {t.addAll(l); return t;});
    }
    public MList<E> forEachR(Consumer<E> function) {
        super.forEach(function);
        return this;
    }
    public MList<E> filter(Predicate<? super E> predicate) {
        return stream().filter(predicate).collect(toMList());
    }
    public Optional<E> getFirst(Predicate<? super E> predicate) {
        MList<E> filtered = filter(predicate);
        if (filtered.size() > 0) {
            return Optional.of(filtered.get(0));
        }
        return Optional.empty();
    }

    public <R> MList<R> map(Function<? super E, ? extends R> function) {
        return stream().map(function).collect(toMList());
    }
    public IntStream mapToInt(ToIntFunction<? super E> toIntFunction) {
        return stream().mapToInt(toIntFunction);
    }
    public LongStream mapToLong(ToLongFunction<? super E> toLongFunction) {
        return stream().mapToLong(toLongFunction);
    }
    public DoubleStream mapToDouble(ToDoubleFunction<? super E> toDoubleFunction) {
        return stream().mapToDouble(toDoubleFunction);
    }
    public <R> MList<R> flatMap(Function<? super E, ? extends Stream<? extends R>> function) {
        return stream().flatMap(function).collect(toMList());
    }
    public IntStream flatMapToInt(Function<? super E, ? extends IntStream> function) {
        return stream().flatMapToInt(function);
    }
    public LongStream flatMapToLong(Function<? super E, ? extends LongStream> function) {
        return stream().flatMapToLong(function);
    }
    public DoubleStream flatMapToDouble(Function<? super E, ? extends DoubleStream> function) {
        return stream().flatMapToDouble(function);
    }
    public MList<E> distinct() {
        return stream().distinct().collect(toMList());
    }
    public MList<E> sorted() {
        return stream().sorted().collect(toMList());
    }
    public MList<E> sorted(Comparator<? super E> comparator) {
        return stream().sorted(comparator).collect(toMList());
    }
    public MList<E> peek(Consumer<? super E> consumer) {
        return stream().peek(consumer).collect(toMList());
    }
    public MList<E> limit(long l) {
        return stream().limit(l).collect(toMList());
    }
    public MList<E> skip(long l) {
        return stream().skip(l).collect(toMList());
    }
    public Object[] toArray() {
        return stream().toArray();
    }
    public <A> A[] toArray(IntFunction<A[]> intFunction) {
        return stream().toArray(intFunction);
    }
    public E reduce(E e, BinaryOperator<E> binaryOperator) {
        return stream().reduce(e, binaryOperator);
    }
    public Optional<E> reduce(BinaryOperator<E> binaryOperator) {
        return stream().reduce(binaryOperator);
    }
    public <U> U reduce(U u, BiFunction<U, ? super E, U> biFunction, BinaryOperator<U> binaryOperator) {
        return stream().reduce(u, biFunction, binaryOperator);
    }
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super E> biConsumer, BiConsumer<R, R> biConsumer1) {
        return stream().collect(supplier, biConsumer, biConsumer1);
    }
    public <R, A> R collect(Collector<? super E, A, R> collector) {
        return stream().collect(collector);
    }
    public Optional<E> min(Comparator<? super E> comparator) {
        return stream().min(comparator);
    }
    public Optional<E> max(Comparator<? super E> comparator) {
        return stream().max(comparator);
    }
    public long count() {
        return stream().count();
    }
    public boolean anyMatch(Predicate<? super E> predicate) {
        return stream().anyMatch(predicate);
    }
    public boolean allMatch(Predicate<? super E> predicate) {
        return stream().allMatch(predicate);
    }
    public boolean noneMatch(Predicate<? super E> predicate) {
        return stream().noneMatch(predicate);
    }
    public Optional<E> findFirst() {
        return stream().findFirst();
    }
    public Optional<E> findAny() {
        return stream().findAny();
    }
    public boolean isParallel() {
        return stream().isParallel();
    }
    public MList<E> sequential() {
        return stream().sequential().collect(toMList());
    }
    public MList<E> parallel() {
        return stream().parallel().collect(toMList());
    }
    public MList<E> unordered() {
        return stream().unordered().collect(toMList());
    }
    public MList<E> onClose(Runnable runnable) {
        return stream().onClose(runnable).collect(toMList());
    }
    public void close() {
        stream().close();
    }
}
