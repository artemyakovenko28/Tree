package com.globallogic.test.tree

import spock.lang.*

import java.util.function.Predicate

@Subject(GeneralTree)
class GeneralTreeSpecification extends Specification {

    Tree.Item<Character> a, b, c, d, e, f, g, h
    GeneralTree<Character> tree

    def setup() {

//         Tree for testing
//
//                   h
//                 / | \
//                /  e  \
//               d        g
//             / | \      |
//            /  |  \     f
//           a   b   c
//
//          tree iteration:
//             - deep first pre order: hdabcegf
//             - deep first post order: abcdefgh
//             - breath first post order: hdegabcf

        tree = new GeneralTree<>('h')
        h = tree.getRoot()
        d = tree.add('d', tree.getRoot())
        a = tree.add('a', d)
        b = tree.add('b', d)
        c = tree.add('c', d)
        e = tree.add('e', tree.getRoot())
        g = tree.add('g', tree.getRoot())
        f = tree.add('f', g)
    }

    def "General methods works correctly"() {
        expect:
        h == tree.getRoot()
        tree.isRoot(h)
        8 == tree.size()
        !tree.isEmpty()
        tree.clear(tree.getRoot())
        0 == tree.size()
    }

    def "Querying tree structure works correctly"() {
        expect:
        tree.contains('d')
        !tree.contains('x')
        !tree.hasChildren(a)
        !tree.hasChildren(f)
        tree.hasChildren(g)
        tree.hasChildren(h)
        d.isParentOf(c)
        tree.isParentOf(d, c)
        tree.isChildOf(e, h)
        a == tree.findChild('a', tree.getRoot())
        null == tree.findChild('z', tree.getRoot())
    }

    def "Removing a node works correctly"() {
        when:
        g == tree.remove('g', tree.getRoot())
        then:
        !tree.contains('g')
        !tree.contains('f')
        6 == tree.size()
    }

    def "Adding a node works correctly"() {
        when: "A node added"
        final Tree.Item<Character> x = tree.add('x', a)
        then: "The tree structure is correct"
        tree.hasChildren(a)
        tree.isParentOf(a, x)
        tree.isChildOf(x, a)
    }

    def "Iterating over a tree works correctly"() {
        char[] chars = new char[8]
        int index = 0
        when: "iterate by deep first pre order strategy"
        def it = tree.iterator(Tree.IterationStrategy.DEPTH_FIRST_PRE, tree.getRoot())
        while (it.hasNext()) {
            chars[index++] = it.next()
        }
        then:
        "hdabcegf" == new String(chars)

        when: "iterate by deep first post order strategy"
        index = 0
        it = tree.iterator(Tree.IterationStrategy.DEPTH_FIRST_POST, tree.getRoot())
        while (it.hasNext()) {
            chars[index++] = it.next()
        }
        then:
        "abcdefgh" == new String(chars)

        when: "iterate by breath first strategy"
        index = 0
        it = tree.iterator(Tree.IterationStrategy.BREATH_FIRST, tree.getRoot())
        while (it.hasNext()) {
            chars[index++] = it.next()
        }
        then:
        "hdegabcf" == new String(chars)
    };

    def "Tree transformed into array correctly"() {
        def result

        when: "transformed by deep first pre order strategy"
        result = tree.toArray(tree.getRoot(), Tree.IterationStrategy.DEPTH_FIRST_PRE)
        then: "The order is maintained"
        "[h, d, a, b, c, e, g, f]" == result.toString()

        when: "transformed by deep first post order strategy"
        result = tree.toArray(tree.getRoot(), Tree.IterationStrategy.DEPTH_FIRST_POST)
        then:
        "[a, b, c, d, e, f, g, h]" == result.toString()

        when: "transformed by breath first strategy"
        result = tree.toArray(tree.getRoot(), Tree.IterationStrategy.BREATH_FIRST)
        then:
        "[h, d, e, g, a, b, c, f]" == result.toString()
    }

    def "Filters works correctly"() {
        Predicate<Character> predicate = { ch -> (ch > 'g') }

        when: "using filter"
        Collection<Character> filtered = tree.filter(predicate)
        then:
        "[h]" == filtered.toString()

        when: "using filter from specified node"
        Collection<Character> filteredFrom = tree.filterFrom(d, predicate)
        then:
        "[]" == filteredFrom.toString()
    }

    def "Deep copy of tree works correctly"() {
        when: "creating subtree"
        Tree<Character> copy = tree.subTree(d)
        then: "elements of tree are not equal by reference"
        GeneralTree.AbstractTreeIterator it = (GeneralTree.AbstractTreeIterator) copy.iterator()
        while (it.hasNext()) {
            Character curr = (Character) it.next()
            Character charA = 'a'
            if (charA == curr) {
                it.getCurrentItem() != a
            }
        }
    }

    def "toString() method of tree works correctly"() {
        expect:
        "[h, d, a, b, c, e, g, f]" == tree.toString()
        "[a, b, c, d, e, f, g, h]" == tree.toString(Tree.IterationStrategy.DEPTH_FIRST_POST)
        "[h, d, e, g, a, b, c, f]" == tree.toString(Tree.IterationStrategy.BREATH_FIRST)
        "[d, a, b, c]" == tree.toString(d)
        "[a, b, c, d]" == tree.toString(Tree.IterationStrategy.DEPTH_FIRST_POST, d)
    }
}
