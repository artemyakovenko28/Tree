package com.globallogic.test.tree;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Standard implementation of Tree
 * @param <T> the type of elements in this tree
 */
public class GeneralTree<T> implements Tree<T> {

    /**
     * default iteration strategy
     */
    private IterationStrategy defaultIterationStrategy = IterationStrategy.DEPTH_FIRST_PRE;

    /**
     * root node
     */
    private Item<T> root;

    /**
     * number of elements in tree
     */
    private int size;

    /**
     * @see com.globallogic.test.tree.Tree.Item
     */
    class Node extends Item<T> {

        /**
         * List of children
         */
        private List<Item<T>> children = new ArrayList<>();

        /**
         * Parent of this node
         */
        private Item<T> parent;

        /**
         * Node value
         */
        private T value;

        /**
         * Default constructor for tree node
         */
        Node() {
        }

        /**
         * Constructor for node with element
         * @param value element to be set
         */
        Node(T value) {
            setValue(value);
        }

        /**
         * all arguments constructor
         * @param children node children
         * @param parent node parent
         * @param value node element
         */
        public Node(List<Item<T>> children, Item<T> parent, T value) {
            this.children = children;
            this.parent = parent;
            this.value = value;
        }

        /**
         * @see Tree#size()
         * @return
         */
        @Override
        int size() {
            int result = 0;
            Iterator<T> it = GeneralTree.this.iterator(defaultIterationStrategy, this);
            while (it.hasNext()) {
                it.next();
                result++;
            }
            return result;
        }

        /**
         * @see Item#contains(Object)
         * @param value element whose presence is to be tested
         * @return
         */
        @Override
        boolean contains(T value) {
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

        /**
         * @see com.globallogic.test.tree.Tree.Item#toArray(Object[], IterationStrategy)
         * @param typeRef type marker. A new array of the same runtime type is allocated for this purpose
         * @param strategy iteration strategy according to which a tree will be traversed when creating an array
         * @return
         */
        @SuppressWarnings({"unchecked", "Duplicates"})
        @Override
        T[] toArray(T[] typeRef, IterationStrategy strategy) {
            final T[] result = (T[]) Array.newInstance(typeRef.getClass().getComponentType(), GeneralTree.this.size);
            int index = 0;

            Iterator<T> it = GeneralTree.this.iterator(strategy, this);
            while (it.hasNext()) {
                result[index++] = it.next();
            }
            return result;
        }

        /**
         * @see Item#toArray(IterationStrategy)
         * @param strategy iteration strategy according to which a tree will be traversed when creating an array
         * @return
         */
        @Override
        Object[] toArray(IterationStrategy strategy) {
            Object[] result = new Object[GeneralTree.this.size];
            int index = 0;

            Iterator<T> it = GeneralTree.this.iterator(strategy, this);
            while (it.hasNext()) {
                result[index++] = it.next();
            }
            return result;
        }

        /**
         * @see Item#clear()
         */
        @Override
        void clear() {
            children.clear();
            value = null;
            parent = null;
        }

        /**
         * @see Item#getValue()
         * @return
         */
        @Override
        T getValue() {
            return value;
        }

        /**
         * @see Item#setValue(Object)
         * @param value supplied value
         * @return
         */
        @Override
        Item<T> setValue(T value) {
            this.value = value;
            return this;
        }

        /**
         * @see Item#hasChildren()
         * @return
         */
        @Override
        boolean hasChildren() {
            return !children.isEmpty();
        }

        /**
         * @see Item#isRoot()
         * @return
         */
        @Override
        boolean isRoot() {
            return parent == null;
        }

        /**
         * @see Item#getParent()
         * @return
         */
        @Override
        Item<T> getParent() {
            return parent;
        }

        /**
         * @see Item#setParent(Item)
         * @param parent node that suppose to be parent node of this node
         * @return
         */
        @Override
        Item<T> setParent(Item<T> parent) {
            this.parent = parent;
            return parent;
        }

        /**
         * @see Item#getChildren()
         * @return
         */
        @Override
        Collection<Item<T>> getChildren() {
            return children;
        }

        /**
         * @see Item#findChild(Object)
         * @param value value that suppose to be find
         * @return
         */
        @Override
        Item<T> findChild(T value) {
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

        /**
         * @see Item#addChild(Object)
         * @param item
         * @return
         */
        @Override
        Item<T> addChild(Item<T> item) {
            children.add(item);
            item.setParent(this);
            return item;
        }

        /**
         * @see Item#removeChild(Object)
         * @param item
         * @return
         */
        @Override
        Item<T> removeChild(Item<T> item) {
            if (item != null && children.remove(item)) {
                item.setParent(null);
                return item;
            }
            return null;
        }

        /**
         * @see Item#addChild(Object)
         * @param value supplied value
         * @return
         */
        @Override
        Item<T> addChild(T value) {
            return addChild(new Node(value));
        }

        /**
         * @see Item#removeChild(Object)
         * @param value supplied value
         * @return
         */
        @Override
        Item<T> removeChild(T value) {
            return removeChild(findChild(value));
        }

        /**
         * @see Item#isParentOf(Item)
         * @param item supplied node
         * @return
         */
        @Override
        boolean isParentOf(Item<T> item) {
            if (item == null) {
                return false;
            }
            return this.equals(item.getParent());
        }

        /**
         * @see Item#isChildOf(Item)
         * @param item supplied node
         * @return
         */
        @Override
        boolean isChildOf(Item<T> item) {
            if (item == null) {
                return false;
            }
            Item<T> tmp = findChild(value);
            return this.equals(tmp);
        }

        /**
         * @see Item#deepCopy()
         * @return
         */
        @Override
        Item<T> deepCopy() {
            Node node = new Node(this.getValue());
            for (Item<T> child : children) {
                node.addChild(child.deepCopy());
            }

            return node;
        }

        /**
         * @see Item#equals(Object)
         * @param o
         * @return
         */
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

        /**
         * @see Item#hashCode()
         * @return
         */
        @Override
        public int hashCode() {
            return Objects.hash(children, parent, value);
        }

        /**
         * @see Item#toString()
         * @return
         */
        @Override
        public String toString() {
            return this.getValue().toString();
        }
    }

    /**
     * Default constructor for GeneralTree
     */
    public GeneralTree() {
        size = 0;
    }

    /**
     * constructor for GeneralTree with root element
     * @param root
     */
    private GeneralTree(Item<T> root) {
        this.root = root;
        size = 1;
    }

    /**
     * constructor for GeneralTree with root value
     * @param rootValue
     */
    public GeneralTree(T rootValue) {
        this.root = new Node(rootValue);
        size = 1;
    }

    /**
     * constructor for GeneralTree with default iteration strategy
     * @param strategy
     */
    public GeneralTree(IterationStrategy strategy) {
        this.defaultIterationStrategy = strategy;
        size = 0;
    }

    /**
     * constructor for GeneralTree with root node and default iteration strategy
     * @param root
     * @param strategy
     */
    public GeneralTree(Item<T> root, IterationStrategy strategy) {
        this.defaultIterationStrategy = strategy;
        this.root = root;
        size = 1;
    }

    /**
     * @see Tree#size()
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @see Tree#isEmpty()
     * @return
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @see Tree#contains(Object, Item)
     * @param value element to test
     * @param parent node from witch start
     * @return
     */
    @Override
    public boolean contains(T value, Item<T> parent) {
        return parent.contains(value);
    }

    /**
     * @see Tree#clear(Item)
     * @param parent supplied node
     */
    @Override
    public void clear(Item<T> parent) {
        int removed = parent.size();
        parent.clear();
        size -= removed;
    }

    /**
     * @see Tree#toArray(Object[], Item, IterationStrategy)
     * @param typeRef type marker. A new array of the same runtime type is allocated for this purpose
     * @param parent starting from this node
     * @param strategy iteration strategy according to which a tree will be traversed when creating an array
     * @return
     */
    @Override
    public T[] toArray(T[] typeRef, Item<T> parent, IterationStrategy strategy) {
        return parent.toArray(typeRef, strategy);
    }

    /**
     * @see Tree#toArray(Item, IterationStrategy)
     * @param parent starting from this node
     * @param strategy iteration strategy according to which a tree will be traversed when creating an array
     * @return
     */
    @Override
    public Object[] toArray(Item<T> parent, IterationStrategy strategy) {
        return parent.toArray(strategy);
    }

    /**
     * @see Tree#add(Object, Item)
     * @param value
     * @param parent parent to which add
     * @return
     */
    @Override
    public Item<T> add(T value, Item<T> parent) {
        Item<T> added = parent.addChild(value);
        size++;
        return added;
    }

    /**
     * @see Tree#remove(Object, Item)
     * @param value
     * @param parent removed element
     * @return
     */
    @Override
    public Item<T> remove(T value, Item<T> parent) {
        Item<T> removed = parent.removeChild(value);
        size = root.size();
        return removed;
    }

    /**
     * @see Tree#getRoot()
     * @return
     */
    @Override
    public Item<T> getRoot() {
        return root;
    }

    /**
     * @see Tree#getChildren(Item)
     * @param item supplied node
     * @return
     */
    @Override
    public Collection<Item<T>> getChildren(Item<T> item) {
        return item.getChildren();
    }

    /**
     * @see Tree#get(Item)
     * @param item specified node
     * @return
     */
    @Override
    public T get(Item<T> item) {
        return item.getValue();
    }

    /**
     * @see Tree#set(Object, Item)
     * @param value specified value
     * @param item specified node
     * @return
     */
    @Override
    public Item<T> set(T value, Item<T> item) {
        return item.setValue(value);
    }

    /**
     * @see Tree#hasChildren(Item)
     * @param item <tt>true</tt> if root
     * @return
     */
    @Override
    public boolean hasChildren(Item<T> item) {
        return item.hasChildren();
    }

    /**
     * @see Tree#isRoot(Item)
     * @param item item to test
     * @return
     */
    @Override
    public boolean isRoot(Item<T> item) {
        return item.isRoot();
    }

    /**
     * @see Tree#getParent(Item)
     * @param item item to test
     * @return
     */
    @Override
    public Item<T> getParent(Item<T> item) {
        return item.getParent();
    }

    /**
     * @see Tree#setParent(Item, Item)
     * @param item node to set parent
     * @param parent parent to set
     * @return
     */
    @Override
    public Item<T> setParent(Item<T> item, Item<T> parent) {
        return item.setParent(parent);
    }

    /**
     * @see Tree#findChild(Object, Item)
     * @param value value that suppose to be find
     * @param parent node from which start search
     * @return
     */
    @Override
    public Item<T> findChild(T value, Item<T> parent) {
        return parent.findChild(value);
    }

    /**
     * @see Tree#isParentOf(Item, Item)
     * @param first first
     * @param second second
     * @return
     */
    @Override
    public boolean isParentOf(Item<T> first, Item<T> second) {
        return first.isParentOf(second);
    }

    /**
     * @see Tree#isChildOf(Item, Item)
     * @param first first
     * @param second second
     * @return
     */
    @Override
    public boolean isChildOf(Item<T> first, Item<T> second) {
        return first.isChildOf(second);
    }

    /**
     * @see Tree#subTree(Item)
     * @param parent root node of subtree
     * @return
     */
    @Override
    public Tree<T> subTree(Item<T> parent) {
        if (parent == null) {
            return null;
        }

        Item<T> parentCopy = parent.deepCopy();
        GeneralTree<T> treeCopy = new GeneralTree<>(parentCopy);
        return treeCopy;
    }

    /**
     * @see Tree#filter(Predicate)
     * @param predicate predicate to test
     * @return
     */
    @Override
    public Collection<T> filter(Predicate<? super T> predicate) {
        return filterFrom(root, predicate);
    }

    /**
     * @see Tree#filterFrom(Item, Predicate)
     * @param parent node from which to start
     * @param predicate predicate to test
     * @return
     */
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

    /**
     * @see Tree#iterator()
     * @return
     */
    @Override
    public Iterator<T> iterator() {
        return iterator(defaultIterationStrategy, root);
    }

    /**
     * @see Tree#iterator(Item)
     * @param parent supplied node
     * @return
     */
    @Override
    public Iterator<T> iterator(Item<T> parent) {
        return iterator(defaultIterationStrategy, parent);
    }

    /**
     * @see Tree#iterator(IterationStrategy)
     * @param strategy supplied iteration strategy
     * @return
     */
    @Override
    public Iterator<T> iterator(IterationStrategy strategy) {
        return iterator(strategy, root);
    }

    /**
     * @see Tree#iterator(IterationStrategy, Item)
     * @param strategy supplied strategy
     * @param item supplied node
     * @return
     */
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

    /**
     * Abstract iterator that is used to retrieving Item<T> during iteration
     */
    public abstract class AbstractTreeIterator implements Iterator<T> {
        /**
         * element that was emitted by next() method
         */
        protected Item<T> currentItem;

        /**
         * getter for element that was emitted by next() method
         * @return
         */
        public Item<T> getCurrentItem() {
            return currentItem;
        }
    }

    /**
     * Iterator for traversing the tree according to deep first pre order iteration strategy
     */
    private class PreOrderIterator extends AbstractTreeIterator {

        /**
         * queue for storing elements during iteration
         */
        final Deque<Item<T>> queue = new LinkedList<>();

        /**
         * constructor with element from which start an iteration
         * @param item element from which start an iteration
         */
        PreOrderIterator(Item<T> item) {
            queue.add(item);
        }

        /**
         * @see Iterator#hasNext()
         * @return
         */
        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        /**
         * @see Iterator#next()
         * @return
         */
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

    /**
     * Iterator for traversing the tree according to deep first post order iteration strategy
     */
    private class PostOrderIterator extends AbstractTreeIterator {

        /**
         * internal queue for storing elements during iteration
         */
        final Deque<Item<T>> first = new LinkedList<>();

        /**
         * internal queue for storing elements during iteration
         */
        final Deque<Item<T>> second = new LinkedList<>();

        /**
         * constructor with element from which start an iteration
         * @param item element from which start an iteration
         */
        public PostOrderIterator(Item<T> item) {
            first.add(item);
        }

        /**
         * @see Iterator#hasNext()
         * @return
         */
        @Override
        public boolean hasNext() {
            return !first.isEmpty() || !second.isEmpty();
        }

        /**
         * @see Iterator#next()
         * @return
         */
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

    /**
     * Iterator for traversing the tree according to breath first iteration strategy
     */
    private class BreathFirstIterator extends AbstractTreeIterator {

        /**
         * internal queue for storing elements during iteration
         */
        final Deque<Item<T>> queue = new LinkedList<>();

        /**
         * constructor with element from which start an iteration
         * @param item element from which start an iteration
         */
        public BreathFirstIterator(Item<T> item) {
            queue.add(item);
        }

        /**
         * @see Iterator#hasNext()
         * @return
         */
        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        /**
         * @see Iterator#next()
         * @return
         */
        @Override
        public T next() {
            Item<T> curr = queue.pollFirst();

            final List<Item<T>> children = (List<Item<T>>) curr.getChildren();
            queue.addAll(children);

            currentItem = curr;
            return curr.getValue();
        }
    }

    /**
     * @see Tree#hashCode()
     * @return
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        for (T item : this)
            hashCode = 31 * hashCode + (item == null ? 0 : item.hashCode());
        return hashCode;
    }

    /**
     * @see Tree#equals(Object)
     * @param that
     * @return
     */
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

    /**
     * @see Tree#toString()
     * @return
     */
    @Override
    public String toString() {
        return toString(defaultIterationStrategy, root);
    }

    /**
     * @see Tree#toString(Item)
     * @param parent supplied node
     * @return
     */
    public String toString(Item<T> parent) {
        return toString(defaultIterationStrategy, parent);
    }

    /**
     * @see Tree#toString(IterationStrategy)
     * @param strategy supplied iteration strategy
     * @return
     */
    public String toString(IterationStrategy strategy) {
        return toString(strategy, root);
    }

    /**
     * @see Tree#toString(IterationStrategy, Item)
     * @param strategy supplied iteration strategy
     * @param parent supplied node
     * @return
     */
    public String toString(IterationStrategy strategy, Item<T> parent) {
        Iterator<T> it = iterator(strategy, parent);
        if (!it.hasNext()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            T e = it.next();
            sb.append(e == this ? "(this Tree)" : e);
            if (!it.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }
}
