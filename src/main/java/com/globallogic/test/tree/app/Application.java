package com.globallogic.test.tree.app;

import com.globallogic.test.tree.Tree;
import com.globallogic.test.tree.GeneralTree;

import java.util.*;
import java.util.function.Predicate;

public class Application {

    public static void main(String[] args) {
        GeneralTree<Character> tree = new GeneralTree<>('h');
        Tree.Item<Character> root = tree.getRoot();
        System.out.println(root);
        Tree.Item<Character> d = tree.add('d', tree.getRoot());
        Tree.Item<Character> a = tree.add('a', d);
        tree.add('b', d);
        tree.add('c', d);
        tree.add('e', tree.getRoot());
        Tree.Item<Character> g = tree.add('g', tree.getRoot());
        tree.add('f', g);

        Object[] pre = tree.toArray(new Character[0], tree.getRoot(), Tree.IterationStrategy.DEPTH_FIRST_PRE);
        System.out.println("Pre order: " + Arrays.toString(pre));

        Character[] breathFirst = tree.toArray(new Character[0], tree.getRoot(), Tree.IterationStrategy.BREATH_FIRST);
        System.out.println("Breath first order: " + Arrays.toString(breathFirst));

        Character[] post = tree.toArray(new Character[0], tree.getRoot(), Tree.IterationStrategy.DEPTH_FIRST_POST);
        System.out.println("Post order: " + Arrays.toString(post));

        System.out.println("contains: " + tree.contains('a', tree.getRoot()));

        System.out.println(tree.findChild('d', tree.getRoot()));

        Predicate<Character> predicate = ch -> ch.compareTo('g') > 0;

        Collection<Character> filtered = tree.filter(predicate);
        System.out.println("filtered: " + filtered);

        Collection<Character> filteredFrom = tree.filterFrom(d, predicate);
        System.out.println("filtered from d: " + filteredFrom);


        Tree<Character> copy = tree.subTree(d);
        System.out.println("copy");
        GeneralTree.AbstractTreeIterator it = (GeneralTree.AbstractTreeIterator) copy.iterator();
        while (it.hasNext()) {
            Character curr = (Character) it.next();
            Character charA = 'a';
            if (charA.equals(curr)) {
                boolean referenceEquals = it.getCurrentItem() == a;
                System.out.println("Reference equals of original and copy for 'a' = " + referenceEquals);
            }
        }
        System.out.println();

        System.out.println("children of root: " + tree.getChildren(tree.getRoot()));
        final Tree.Item<Character> x = tree.add('x', a);
        System.out.println("children of a: " + tree.getChildren(a));
        System.out.println(tree.hasChildren(a));
    }
}
