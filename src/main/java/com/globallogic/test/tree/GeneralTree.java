package com.globallogic.test.tree;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GeneralTree<T> implements Tree<T> {

    private IterationStrategy defaultIterationStrategy = IterationStrategy.DEPTH_FIRST_PRE;

    private Item<T> root;
    private int size;

    class Node extends Item<T> {

        private List<Item<T>> children = new ArrayList<>();
        private Item<T> parent;
        private T value;

        Node() {
        }

        Node(T value) {
            setValue(value);
        }

        public Node(List<Item<T>> children, Item<T> parent, T value) {
            this.children = children;
            this.parent = parent;
            this.value = value;
        }

        @Override
        public int size() {
            int result = 0;
            Iterator<T> it = GeneralTree.this.iterator(defaultIterationStrategy, this);
            while (it.hasNext()) {
                it.next();
                result++;
            }
            return result;
        }

        @Override
        public boolean contains(T value) {
            boolean result = false;
            Iterator<T> it = GeneralTree.this.iterator(defaultIterationStrategy, this);
            while (it.hasNext()) {
                if (it.next().equals(value)) {
                    result = true;
                    break;
                }
            }
            return result;
        }

        @SuppressWarnings({"unchecked", "Duplicates"})
        @Override
        public T[] toArray(T[] typeRef, IterationStrategy strategy) {
            final T[] result = (T[]) Array.newInstance(typeRef.getClass().getComponentType(), GeneralTree.this.size);
            int index = 0;

            Iterator<T> it = GeneralTree.this.iterator(strategy, this);
            while (it.hasNext()) {
                result[index++] = it.next();
            }
            return result;
        }

        @Override
        public Object[] toArray(IterationStrategy strategy) {
            Object[] result = new Object[GeneralTree.this.size];
            int index = 0;

            Iterator<T> it = GeneralTree.this.iterator(strategy, this);
            while (it.hasNext()) {
                result[index++] = it.next();
            }
            return result;
        }

        @Override
        public void clear() {
            children.clear();
            value = null;
            parent = null;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public Item<T> setValue(T value) {
            this.value = value;
            return this;
        }

        @Override
        public boolean hasChildren() {
            return !children.isEmpty();
        }

        @Override
        public boolean isRoot() {
            return parent == null;
        }

        @Override
        public Item<T> getParent() {
            return parent;
        }

        @Override
        public Item<T> setParent(Item<T> parent) {
            this.parent = parent;
            return parent;
        }

        @Override
        public Collection<Item<T>> getChildren() {
            return children;
        }

        @Override
        public Item<T> findChild(T value) {
            Item<T> result = null;

            AbstractTreeIterator it = (AbstractTreeIterator) GeneralTree.this.iterator(defaultIterationStrategy, this);
            while (it.hasNext()) {
                it.next();
                Item<T> item = it.getCurrentItem();
                if (item.getValue().equals(value)) {
                    result = item;
                    break;
                }
            }

            return result;
        }

        @Override
        public Item<T> addChild(Item<T> item) {
            children.add(item);
            item.setParent(this);
            return item;
        }

        @Override
        public Item<T> removeChild(Item<T> item) {
            if (item != null && children.remove(item)) {
                item.setParent(null);
                return item;
            }
            return null;
        }

        @Override
        public Item<T> addChild(T value) {
            return addChild(new Node(value));
        }

        @Override
        public Item<T> removeChild(T value) {
            return removeChild(findChild(value));
        }

        @Override
        public boolean isParentOf(Item<T> item) {
            if (item == null) {
                return false;
            }
            return this.equals(item.getParent());
        }

        @Override
        public boolean isChildOf(Item<T> item) {
            if (item == null) {
                return false;
            }
            Item<T> tmp = findChild(value);
            return this.equals(tmp);
        }

        @Override
        Item<T> deepCopy() {
            Node node = new Node(this.getValue());
            for (Item<T> child : children) {
                node.addChild(child.deepCopy());
            }

            return node;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;

            List<T> values = children.stream()
                    .map(Item::getValue)
                    .collect(Collectors.toList());
            List<T> nodeValues = node.children.stream()
                    .map(Item::getValue)
                    .collect(Collectors.toList());

            return Objects.equals(values, nodeValues) &&
                    Objects.equals(parent, node.parent) &&
                    Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(children, parent, value);
        }

        @Override
        public String toString() {
            return this.getValue().toString();
        }
    }

    public GeneralTree() {
        size = 0;
    }

    private GeneralTree(Item<T> root) {
        this.root = root;
        size = 1;
    }

    public GeneralTree(T rootValue) {
        this.root = new Node(rootValue);
        size = 1;
    }

    public GeneralTree(IterationStrategy strategy) {
        this.defaultIterationStrategy = strategy;
        size = 0;
    }

    public GeneralTree(Item<T> root, IterationStrategy strategy) {
        this.defaultIterationStrategy = strategy;
        this.root = root;
        size = 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T value, Item<T> parent) {
        return parent.contains(value);
    }

    @Override
    public void clear(Item<T> parent) {
        int removed = parent.size();
        parent.clear();
        size -= removed;
    }

    @Override
    public T[] toArray(T[] typeRef, Item<T> parent, IterationStrategy strategy) {
        return parent.toArray(typeRef, strategy);
    }

    @Override
    public Object[] toArray(Item<T> parent, IterationStrategy strategy) {
        return parent.toArray(strategy);
    }

    @Override
    public Item<T> add(T value, Item<T> parent) {
        Item<T> added = parent.addChild(value);
        size++;
        return added;
    }

    @Override
    public Item<T> remove(T value, Item<T> parent) {
        Item<T> removed = parent.removeChild(value);
        size = root.size();
        return removed;
    }

    @Override
    public Item<T> getRoot() {
        return root;
    }

    @Override
    public Collection<Item<T>> getChildren(Item<T> item) {
        return item.getChildren();
    }

    @Override
    public T get(Item<T> item) {
        return item.getValue();
    }

    @Override
    public Item<T> set(T value, Item<T> item) {
        return item.setValue(value);
    }

    @Override
    public boolean hasChildren(Item<T> item) {
        return item.hasChildren();
    }

    @Override
    public boolean isRoot(Item<T> item) {
        return item.isRoot();
    }

    @Override
    public Item<T> getParent(Item<T> item) {
        return item.getParent();
    }

    @Override
    public Item<T> setParent(Item<T> item, Item<T> parent) {
        return item.setParent(parent);
    }

    @Override
    public Item<T> findChild(T value, Item<T> parent) {
        return parent.findChild(value);
    }

    @Override
    public boolean isParentOf(Item<T> first, Item<T> second) {
        return first.isParentOf(second);
    }

    @Override
    public boolean isChildOf(Item<T> first, Item<T> second) {
        return first.isChildOf(second);
    }

    @Override
    public Tree<T> subTree(Item<T> parent) {
        if (parent == null) {
            return null;
        }

        Item<T> parentCopy = parent.deepCopy();
        GeneralTree<T> treeCopy = new GeneralTree<>(parentCopy);
        return treeCopy;
    }

    @Override
    public Collection<T> filter(Predicate<? super T> predicate) {
        return filterFrom(root, predicate);
    }

    @Override
    public Collection<T> filterFrom(Item<T> parent, Predicate<? super T> predicate) {
        Collection<T> result = new ArrayList<>();
        Iterator<T> it = iterator(parent);

        while (it.hasNext()) {
            T curr = it.next();
            if (predicate.test(curr)) {
                result.add(curr);
            }
        }

        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return iterator(defaultIterationStrategy, root);
    }

    @Override
    public Iterator<T> iterator(Item<T> parent) {
        return iterator(defaultIterationStrategy, parent);
    }

    @Override
    public Iterator<T> iterator(IterationStrategy strategy) {
        return iterator(strategy, root);
    }

    @Override
    public Iterator<T> iterator(IterationStrategy strategy, Item<T> item) {
        if (IterationStrategy.DEPTH_FIRST_PRE.equals(strategy)) {
            return new PreOrderIterator(item);
        } else if (IterationStrategy.DEPTH_FIRST_POST.equals(strategy)) {
            return new PostOrderIterator(item);
        } else if (IterationStrategy.BREATH_FIRST.equals(strategy)) {
            return new BreathFirstIterator(item);
        } else {
            throw new IllegalArgumentException(String.format("%s: no such enum constant for enum %s",
                    strategy, IterationStrategy.class.getName()));
        }
    }

    public abstract class AbstractTreeIterator implements Iterator<T> {
        protected Item<T> currentItem;

        public Item<T> getCurrentItem() {
            return currentItem;
        }
    }

    private class PreOrderIterator extends AbstractTreeIterator {

        final Deque<Item<T>> queue = new LinkedList<>();

        PreOrderIterator(Item<T> item) {
            queue.add(item);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            Item<T> curr = queue.pollLast();

            final List<Item<T>> children = (List<Item<T>>) curr.getChildren();

            ListIterator<Item<T>> it = children.listIterator(children.size());
            while (it.hasPrevious()) {
                queue.add(it.previous());
            }

            currentItem = curr;
            return curr.getValue();
        }
    }

    private class PostOrderIterator extends AbstractTreeIterator {

        final Deque<Item<T>> first = new LinkedList<>();
        final Deque<Item<T>> second = new LinkedList<>();

        public PostOrderIterator(Item<T> item) {
            first.add(item);
        }

        @Override
        public boolean hasNext() {
            return !first.isEmpty() || !second.isEmpty();
        }

        @Override
        public T next() {
            while (!first.isEmpty()) {
                Item<T> curr = first.pollLast();
                second.add(curr);

                final List<Item<T>> children = (List<Item<T>>) curr.getChildren();
                first.addAll(children);
            }

            Item<T> item = second.pollLast();
            currentItem = item;
            return item.getValue();
        }
    }

    private class BreathFirstIterator extends AbstractTreeIterator {

        final Deque<Item<T>> queue = new LinkedList<>();

        public BreathFirstIterator(Item<T> item) {
            queue.add(item);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public T next() {
            Item<T> curr = queue.pollFirst();

            final List<Item<T>> children = (List<Item<T>>) curr.getChildren();
            queue.addAll(children);

            currentItem = curr;
            return curr.getValue();
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (T item : this)
            hashCode = 31 * hashCode + (item == null ? 0 : item.hashCode());
        return hashCode;
    }

    @Override
    public boolean equals(Object that) {
        if (that == this) {
            return true;
        }
        if (!(that instanceof Tree)) {
            return false;
        }

        Iterator<T> it1 = iterator();
        Iterator<?> it2 = ((Tree<?>) that).iterator();
        while (it1.hasNext() && it2.hasNext()) {
            T item1 = it1.next();
            Object item2 = it2.next();
            if (!(Objects.equals(item1, item2))) {
                return false;
            }
        }
        return !(it1.hasNext() || it2.hasNext());
    }
}
