package com.globallogic.test.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Hierarchical tree structure, with a root value and subtrees
 * of children with a parent node, represented as a structure of linked nodes.
 * Generic container for items in a tree of arbitrary structure
 * @param <T> the type of elements in this tree
 */
public interface Tree<T> extends Iterable<T> {

    /**
     * Available algorithms for traversing a tree
     *
     * DEPTH_FIRST_PRE -
     * Check if the current node is empty or null.
     * Traverse the left subtree by recursively calling the post-order function.
     * Traverse the right subtree by recursively calling the post-order function.
     * Display the data part of the root (or current node).
     * Steps: left, right, visit
     *
     * DEPTH_FIRST_POST -
     * Check if the current node is empty or null.
     * Display the data part of the root (or current node).
     * Traverse the left subtree by recursively calling the pre-order function.
     * Traverse the right subtree by recursively calling the pre-order function.
     * Steps: visit, left, right
     *
     * BREATH_FIRST - it starts at the tree root, and explores
     * all of the neighbor nodes at the present depth prior to moving
     * on to the nodes at the next depth level.
     * Steps: level order from left to right
     */
    enum IterationStrategy {
        DEPTH_FIRST_PRE,
        DEPTH_FIRST_POST,
        BREATH_FIRST
    }

    /**
     * Represents a node of a tree
     * @param <T> the type of value in this node
     */
    abstract class Item<T> {

        /**
         * Returns the number of elements starting from this node
         * @return number of elements
         */
        abstract int size();

        /**
         * Returns <tt>true</tt> if this node has child that contains the specified element
         * @param value element whose presence is to be tested
         * @return <tt>true</tt> if this node has child that contains the specified element
         */
        abstract boolean contains(T value);

        /**
         * Returns an array containing all of the elements in this list in
         * proper sequence according to supplied iteration strategy
         * @param typeRef type marker. A new array of the same runtime type is allocated for this purpose
         * @param strategy iteration strategy according to which a tree will be traversed when creating an array
         * @return an array containing the elements of this tree
         */
        abstract T[] toArray(T[] typeRef, IterationStrategy strategy);

        /**
         * Returns an array containing all of the elements in this list in
         * proper sequence according to supplied iteration strategy
         * @param strategy iteration strategy according to which a tree will be traversed when creating an array
         * @return an array containing the elements of this tree
         */
        abstract Object[] toArray(IterationStrategy strategy);

        /**
         * Removes all of the elements starting from root node
         * The structure will be empty after this call returns.
         */
        abstract void clear();

        /**
         * Returns a value of a node
         * @return value of a node
         */
        abstract T getValue();

        /**
         * Sets a value to this node
         * @param value supplied value
         * @return value that has been set
         */
        abstract Item<T> setValue(T value);

        /**
         * Checks whether node has children. Returns <tt>true</tt> if it has
         * @return <tt>true</tt> if node has children
         */
        abstract boolean hasChildren();

        /**
         * Checks whether node is a root node in tree
         * @return <tt>true</tt> if root
         */
        abstract boolean isRoot();

        /**
         * Returns parent node of this node
         * @return parent node
         */
        abstract Item<T> getParent();

        /**
         * Sets a parent node of this node
         * @param parent node that suppose to be parent node of this node
         * @return parent node that has been set
         */
        abstract Item<T> setParent(Item<T> parent);

        /**
         * Get collection of children of this node
         * @return collection of children
         */
        abstract Collection<Item<T>> getChildren();

        /**
         * Finds the first occurrence of supplied element according to default iteration strategy
         * @param value value that suppose to be find
         * @return first occurrence of supplied element according to default iteration strategy.
         * <tt>null</tt> otherwise
         */
        abstract Item<T> findChild(T value);

        /**
         * Adds child to this node
         * @param child supplied node
         * @return added node
         */
        abstract Item<T> addChild(Item<T> child);

        /**
         * Removes child of this node
         * @param child  supplied node
         * @return removed node
         */
        abstract Item<T> removeChild(Item<T> child);

        /**
         * Adds child to this node
         * @param value supplied value
         * @return added node
         */
        abstract Item<T> addChild(T value);

        /**
         * Removes child of this node
         * @param value supplied value
         * @return removed node
         */
        abstract Item<T> removeChild(T value);

        /**
         * Check whether this node is parent of supplied
         * @param item supplied node
         * @return <tt>true</tt> of this node is parent of supplied. <tt>false</tt> otherwise
         */
        abstract boolean isParentOf(Item<T> item);

        /**
         * Check whether this node is child of supplied
         * @param item supplied node
         * @return <tt>true</tt> of this node is child of supplied. <tt>false</tt> otherwise
         */
        abstract boolean isChildOf(Item<T> item);

        /**
         * Creates deep copy of tree starting from this node
         * @return copy of tree structure
         */
        abstract Item<T> deepCopy();
    }

    /**
     * Returns the number of elements in tree
     * @return the number of elements starting from this node
     */
    int size();

    /**
     * Returns <tt>true</tt> if this list contains no elements
     * @return <tt>true</tt> if this list contains no elements.
     */
    boolean isEmpty();

    /**
     * Returns <tt>true</tt> if tree has child that contains the specified element starting from supplied node
     * @param value element to test
     * @param parent node from witch start
     * @return <tt>true</tt> if tree has child that contains the specified element starting from supplied node
     */
    boolean contains(T value, Item<T> parent);

    /**
     * Removes all of the elements starting from supplied node.
     * @param parent supplied node
     */
    void clear(Item<T> parent);

    /**
     * Returns an array containing all of the elements in this tree in
     * proper sequence according to supplied iteration strategy
     * @param typeRef type marker. A new array of the same runtime type is allocated for this purpose
     * @param parent starting from this node
     * @param strategy iteration strategy according to which a tree will be traversed when creating an array
     * @return an array containing the elements of this tree
     */
    T[] toArray(T[] typeRef, Item<T> parent, IterationStrategy strategy);

    /**
     * Returns an array containing all of the elements in this tree in
     * proper sequence according to supplied iteration strategy
     * @param parent starting from this node
     * @param strategy iteration strategy according to which a tree will be traversed when creating an array
     * @return
     */
    Object[] toArray(Item<T> parent, IterationStrategy strategy);

    /**
     * Add child of particular parent
     * @param item element to be added
     * @param parent parent to which add
     * @return added element
     */
    Item<T> add(T item, Item<T> parent);

    /**
     * Remove child of particular parent
     * @param item element to be removed
     * @param parent removed element
     * @return
     */
    Item<T> remove(T item, Item<T> parent);

    /**
     * Get the root of this tree
     * @return root of this tree
     */
    Item<T> getRoot();

    /**
     * Get collection of children of this node
     * @param item supplied node
     * @return collection of children of this node
     */
    Collection<Item<T>> getChildren(Item<T> item);

    /**
     * get node value
     * @param item specified node
     * @return specified node value
     */
    T get(Item<T> item);

    /**
     * set node value
     * @param value specified value
     * @param item specified node
     * @return node that has been updated
     */
    Item<T> set(T value, Item<T> item);

    /**
     * Checks whether node is a root node in tree
     * @param item <tt>true</tt> if root
     * @return
     */
    boolean hasChildren(Item<T> item);

    /**
     * Checks whether node is a root node in tree
     * @param item item to test
     * @return <tt>true</tt> if root
     */
    boolean isRoot(Item<T> item);

    /**
     * Returns parent node of specified node in tree
     * @param item item to test
     * @return parent node of specified node in tree
     */
    Item<T> getParent(Item<T> item);

    /**
     * Sets a parent node of this node
     * @param item node to set parent
     * @param parent parent to set
     * @return parent that has been set
     */
    Item<T> setParent(Item<T> item, Item<T> parent);

    /**
     * Finds the first occurrence of supplied element according to default iteration strategy
     * @param value value that suppose to be find
     * @param parent node from which start search
     * @return first occurrence of supplied element according to default iteration strategy.
     */
    Item<T> findChild(T value, Item<T> parent);

    /**
     * Check whether first node is parent of second
     * @param first first
     * @param second second
     * @return <tt>true</tt> of first node is parent of second. <tt>false</tt> otherwise
     */
    boolean isParentOf(Item<T> first, Item<T> second);

    /**
     *Check whether first node is child of second
     * @param first first
     * @param second second
     * @return <tt>true</tt> of first node is child of second. <tt>false</tt> otherwise
     */
    boolean isChildOf(Item<T> first, Item<T> second);

    /**
     * Creates a deep copy of tree starting from supplied node
     * @param parent root node of subtree
     * @return new tree instance
     */
    Tree<T> subTree(Item<T> parent);

    /**
     * Returns a collection of elements that matches supplied predicate
     * @param predicate predicate to test
     * @return a collection of elements that matches supplied predicate
     */
    Collection<T> filter(Predicate<? super T> predicate);

    /**
     * Returns a collection of elements that matches supplied predicate starting from specified parent
     * @param parent node from which to start
     * @param predicate predicate to test
     * @return a collection of elements that matches supplied predicate starting from specified parent
     */
    Collection<T> filterFrom(Item<T> parent, Predicate<? super T> predicate);

    /**
     * Returns an iterator over the elements in this tree in proper sequence
     * according to default iteration strategy starting from root
     * @return an iterator over the elements in this tree in proper sequence
     * according to default iteration strategy starting from root
     */
    @Override
    Iterator<T> iterator();

    /**
     * Returns an iterator over the elements in this tree in proper sequence
     * according to default iteration strategy starting from specified node
     * @param parent supplied node
     * @return an iterator over the elements in this tree in proper sequence
     * according to default iteration strategy starting from specified node
     */
    Iterator<T> iterator(Item<T> parent);

    /**
     * Returns an iterator over the elements in this tree in proper sequence
     * according to specified iteration strategy starting from root
     * @param strategy supplied iteration strategy
     * @return an iterator over the elements in this tree in proper sequence
     * according to specified iteration strategy starting from root
     */
    Iterator<T> iterator(IterationStrategy strategy);

    /**
     * Returns an iterator over the elements in this tree in proper sequence
     * according to specified iteration strategy starting from specified node
     * @param strategy supplied strategy
     * @param item supplied node
     * @return an iterator over the elements in this tree in proper sequence
     * according to specified iteration strategy starting from specified node
     */
    Iterator<T> iterator(IterationStrategy strategy, Item<T> item);

    /**
     * @see Object#equals(Object)
     * @param o object to be compared
     * @return <tt>true</tt> if this tree is equal to supplied object. <tt>false</tt> otherwise
     */
    boolean equals(Object o);

    /**
     * @see Object#hashCode()
     * @return
     */
    int hashCode();

    /**
     * Returns string representation of this tree according default iteration strategy
     * starting from specified node
     * @param parent supplied node
     * @return string representation of this tree according default iteration strategy
     * starting from specified node
     */
    String toString(Item<T> parent);

    /**
     * Returns string representation of this tree according specified iteration strategy
     * starting from root
     * @param strategy supplied iteration strategy
     * @return string representation of this tree according specified iteration strategy
     * starting from root
     */
    String toString(IterationStrategy strategy);

    /**
     * Returns string representation of this tree according specified iteration strategy
     * starting from specified node
     * @param strategy supplied iteration strategy
     * @param parent supplied node
     * @return string representation of this tree according specified iteration strategy
     * starting from specified node
     */
    String toString(IterationStrategy strategy, Item<T> parent);
}
