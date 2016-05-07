package com.company;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

/**
 * Binary Search Tree Implementation
 * This is an unbalanced tree, for balanced implementation, see MTSplayTree
 * No duplicate is allowed.
 * Data store in this tree is required to implement Comparable<T>
 * All methods logic is based on compareTo method
 *
 * Convention: the left child is less than or equal to its parent
 */
public class MTBinarySearchTree<E extends Comparable<E>> implements Iterable<E> {

    private Node<E> root;
    int size;

    public MTBinarySearchTree() {}

    private MTBinarySearchTree(Node<E> root) {
        this.root = root;
        this.size = size(root);
    }

    private int size(Node<E> cur) {
        if(cur == null)
            return 0;
        return 1 + size(cur.left) + size(cur.right);
    }

    /**
     * Get the k-th (0 indexed) smallest value, null if
     * there is no such value
     */
    public E select(int k) {
        return select(root, k);
    }

    private E select(Node<E> cur, int k) {
        if(cur == null)
            return null;
        int leftSize = size(cur.left);
        if(leftSize == k)
            return cur.value;
        if(leftSize > k)
            return select(cur.left, k);
        return select(cur.right, k - leftSize - 1);
    }

    /**
     * Recover the tree given its in-order traversal
     * and post-order traversal.
     * In-order and Post-order must have the same length
     * and contain distinct elements
     *
     * Notes: both BinaryTree and BST require in-order and
     * post-order (or pre-order) to be uniquely recovered
     */
    public static <V extends Comparable<V>>MTBinarySearchTree<V> recover(V []inorder, V[]postorder) {
        int postorderRoot = postorder.length - 1;
        Node<V> root = recover(inorder, postorder, postorderRoot, 0, inorder.length-1);
        return new MTBinarySearchTree<>(root);
    }

    private static <V extends Comparable<V>> Node<V> recover(V[] inorder, V[] postorder, int pos, int low, int hi) {
        if (low > hi)
            return null;
        Node<V> root = new Node<>(postorder[pos]);

        int inorderRoot = Arrays.binarySearch(inorder, root.value);
        int rightSize = hi - inorderRoot;

        root.left = recover(inorder, postorder, pos - rightSize - 1, low, inorderRoot - 1);
        root.right = recover(inorder, postorder, pos - 1, inorderRoot + 1, hi);
        return root;
    }

    /**
     * The longest path between 2 nodes in the tree
     */
    public int diameter() {
        return diameter(root);
    }

    private int diameter(Node<E> cur) {
        if(cur == null)
            return 0;
        // if the longest path go through here
        int here = height(cur.left) + height(cur.right);

        // if the longest path is in the subtrees
        return Math.max(Math.max(diameter(cur.left), diameter(cur.right)), here);
    }

    /**
     * Maximum number of nodes on ONE level
     */
    public int treeWidth() {
        int height = height();
        int width = 0;
        for(int h = 1; h <= height; ++h)
            width = Math.max(treeWidth(root, h), width);
        return width;
    }

    private int treeWidth(Node<E> cur, int height) {
        if(cur == null)
            return 0;
        if(height == 0)
            return 1;
        return treeWidth(cur.left, height - 1) + treeWidth(cur.right, height - 1);
    }

    /**
     * Count the number of leaf nodes
     */
    public int leavesCount() {
        return leavesCount(root);
    }

    private int leavesCount(Node<E> cur) {
        if(cur == null)
            return 0;
        if(cur.left == null && cur.right == null)
            return 1;
        return leavesCount(cur.left) + leavesCount(cur.right);
    }

    /**
     * Return the smallest element greater than or equal to @value
     * or null if there is no such element
     */
    public E ceiling(E value) {
        Node<E> result = ceiling(root, value);
        return (result != null) ? result.value : null;
    }

    private Node<E> ceiling(Node<E> cur, E value) {
        if(cur == null)
            return null;
        int compare = cur.value.compareTo(value);
        if(compare == 0)
            return cur;
        if(compare < 0)
            return ceiling(cur.right, value);

        // find on the left subtree
        Node<E> left = ceiling(cur.left, value);
        return (left != null) ? left : cur;
    }

    /**
     * Distance from node contains @first to the one
     * contains @second
     */
    public int distance(E first, E second) {
        Node<E> parent = lca(root, first, second);
        int left = heightFor(parent, first);
        int right = heightFor(parent, second);
        return left + right;
    }

    /**
     * The height of node contains @value in the subtree
     * rooted at @cur
     */
    private int heightFor(Node<E> cur, E value) {
        int compare = cur.value.compareTo(value);
        if(compare == 0)
            return 0; // found!
        if(compare > 0)
            return 1 + heightFor(cur.left, value);
        return 1 + heightFor(cur.right, value);
    }

    public int height() {
        return height(root);
    }

    private int height(Node cur) {
        if(cur == null)
            return 0;
        return Math.max(height(cur.left), height(cur.right)) + 1;
    }

    public E lca(E first, E second) {
        if(first.compareTo(second) <= 0)
            return lca(root, first, second).value;
        return lca(root, second, first).value;
    }

    private Node<E> lca(Node<E> cur, E first, E second) {
        if(cur == null)
            return null; // not found in this subtree

        // if both values is smaller then LCA is in the left subtree
        if(cur.value.compareTo(first) > 0 && cur.value.compareTo(second) > 0)
            return lca(cur.left, first, second);
        // vice versa
        if(cur.value.compareTo(first) < 0 && cur.value.compareTo(second) < 0)
            return lca(cur.right, first, second);

        return cur;
    }

    /**
     * Remove @value from the tree.
     * @return true if @value exists in the tree, false otherwise.
     */
    public boolean remove(E value) {
        int oldSize = size;
        root = remove(root, value);
        return oldSize > size;
    }

    private Node<E> remove(Node<E> cur, E value) {
        if(cur == null)
            return null;
        int compare = cur.value.compareTo(value);
        if(compare == 0) {
            // case 1: only 1 children
            if(cur.left == null)
                return cur.right;
            else if(cur.right == null)
                return cur.left;

            // case 2: 2 children
            Node<E> candidate = findLargest(cur.left);
            copyValue(candidate, cur);
            size--;

            // then remove the candidate
            cur.left = remove(cur.left, candidate.value);
        }
        if(compare > 0)
            cur.left = remove(cur.left, value);
        else
            cur.right = remove(cur.right, value);
        return cur;
    }

    /**
     * @param target find parent of this node
     */
    private Node<E> findParent(Node<E> cur, Node<E> target) {
        if(cur == null)
            return null;
        if(cur.left == target || cur.right == target)
            return cur;
        int compare = cur.value.compareTo(target.value);
        if(compare >= 0)
            return findParent(cur.left, target);
        return findParent(cur.right, target);
    }

    /**
     * Find the largest node in the subtree rooted at 'cur'
     * @param cur root of the subtree
     */
    private Node<E> findLargest(Node<E> cur) {
        if(cur.right != null)
            return findLargest(cur.right);
        return cur;
    }

    private void copyValue(Node<E> src, Node<E> des) {
        des.value = src.value;
    }

    public boolean contains(E value) {
        if(root == null)
            return false;
        return contains(root, value);
    }

    private boolean contains(Node<E> cur, E value) {
        if(cur == null)
            return false;
        int compare = cur.value.compareTo(value);
        if(compare == 0)
            return true;
        if(compare > 0)
            return contains(cur.left, value);
        return contains(cur.right, value);
    }

    public void add(E value) {
        if(contains(root, value))
            return; // duplicate is not allowed
        if(root == null)
            root = new Node<>(value);
        else
            add(root, value);
        size++;
    }

    private Node<E> add(Node<E> cur, E value) {
        if(cur == null)
            return new Node<>(value);
        if(cur.value.compareTo(value) >= 0) // left child less than or equal
            cur.left = add(cur.left, value);
        else
            cur.right = add(cur.right, value);
        return cur;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb);
        return sb.append(String.format("(size = %d)", size)).toString();
    }

    public String toCustomString(TreeStringType type) {
        StringBuilder sb = new StringBuilder();
        if(type == TreeStringType.INORDER)
            toString(root, sb);
        else if(type == TreeStringType.PREORDER)
            toStringPreorder(root, sb);
        else if(type == TreeStringType.POSTORDER)
            toStringPostorder(root, sb);

        return sb.append(String.format("(size = %d)", size)).toString();
    }

    private void toString(Node<E> cur, StringBuilder sb) {
        if(cur == null)
            return;
        toString(cur.left, sb);
        sb.append(cur.value).append(" ");
        toString(cur.right, sb);
    }

    private void toStringPreorder(Node<E> cur, StringBuilder sb) {
        if(cur == null)
            return;
        sb.append(cur.value).append(" ");
        toStringPreorder(cur.left, sb);
        toStringPreorder(cur.right, sb);
    }

    private void toStringPostorder(Node<E> cur, StringBuilder sb) {
        if(cur == null)
            return;
        toStringPostorder(cur.left, sb);
        toStringPostorder(cur.right, sb);
        sb.append(cur.value).append(" ");
    }

    @Override
    public Iterator<E> iterator() {
        return new MTIterator();
    }

    /**
     * Pre-order traversal
     */
    private class MTIterator implements Iterator<E>{
        private Stack<Node<E>> stack = new Stack<>();
        {
            if(root != null)
                stack.add(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() {
            Node<E> node = stack.pop();
            if(node.right != null)
                stack.add(node.right);
            if(node.left != null)
                stack.add(node.left);
            return node.value;
        }
    }

    static class Node<T extends Comparable<T>> {
        private T value;
        private Node<T> left, right;

        private Node(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    enum TreeStringType {
        INORDER, POSTORDER, PREORDER
    }
}
