package com.globallogic.test.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

public interface Tree<T> extends Iterable<T> {

    enum IterationStrategy {
        DEPTH_FIRST_PRE,
        DEPTH_FIRST_POST,
        BREATH_FIRST
    }

    abstract class Item<T> {

        abstract int size();

        abstract boolean contains(T value);

        abstract T[] toArray(T[] typeRef, IterationStrategy strategy);

        abstract Object[] toArray(IterationStrategy strategy);

        abstract void clear();

        abstract T getValue();

        abstract Item<T> setValue(T value);

        abstract boolean hasChildren();

        abstract boolean isRoot();

        abstract Item<T> getParent();

        abstract Item<T> setParent(Item<T> parent);

        abstract Collection<Item<T>> getChildren();

        abstract Item<T> findChild(T value);

        abstract Item<T> addChild(Item<T> child);

        abstract Item<T> removeChild(Item<T> child);

        abstract Item<T> addChild(T value);

        abstract Item<T> removeChild(T value);

        abstract boolean isParentOf(Item<T> item);

        abstract boolean isChildOf(Item<T> item);

        abstract Item<T> deepCopy();
    }

    int size();

    boolean isEmpty();

    boolean contains(T value, Item<T> parent);

    void clear(Item<T> parent);

    T[] toArray(T[] typeRef, Item<T> parent, IterationStrategy strategy);

    Object[] toArray(Item<T> parent, IterationStrategy strategy);

    Item<T> add(T item, Item<T> parent);

    Item<T> remove(T item, Item<T> parent);

    Item<T> getRoot();

    Collection<Item<T>> getChildren(Item<T> item);

    T get(Item<T> item);

    Item<T> set(T value, Item<T> item);
    
    boolean hasChildren(Item<T> item);
    
    boolean isRoot(Item<T> item);
    
    Item<T> getParent(Item<T> item);
    
    Item<T> setParent(Item<T> item, Item<T> parent);

    Item<T> findChild(T value, Item<T> parent);
    
    boolean isParentOf(Item<T> first, Item<T> second);
    
    boolean isChildOf(Item<T> first, Item<T> second);

    Tree<T> subTree(Item<T> parent);

    Collection<T> filter(Predicate<? super T> predicate);

    Collection<T> filterFrom(Item<T> parent, Predicate<? super T> predicate);

    @Override
    Iterator<T> iterator();

    Iterator<T> iterator(Item<T> parent);

    Iterator<T> iterator(IterationStrategy strategy);

    Iterator<T> iterator(IterationStrategy strategy, Item<T> item);

    boolean equals(Object o);

    int hashCode();
}
