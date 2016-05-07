package com.company;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Optional;

public class Main {

    static PrintStream out = System.out;
    static String rightPad = "%-30s -> %s\n";
    static MTBinarySearchTree<Integer> tree;

    public static void main(String[] args) {
        int []data = {10, 5, 12, 14, 1, 8, 6, 9, 20, 18, 16, 19, 22};

        tree = new MTBinarySearchTree<>();
        for(int i : data)
            tree.add(i);

        out.printf("Init:\n%s\n", tree);

        out.println("\nFinding tests\n------------------");
        containsTest(10, true); // 5 is in the tree
        containsTest(2, false); // 2 is not in the tree
        containsTest(22, true);

        out.println("\nRemoval tests\n------------------");
        removeTest(10, true);
        removeTest(2, false);
        removeTest(20, true);
        out.printf("After removal:\n%s\n", tree);

        out.println("\nLCA tests\n------------------");
        lcaTest(1, 19, 9);
        lcaTest(5, 8, 5);
        lcaTest(1, 6, 5);
        lcaTest(16, 16, 16);

        out.println("\nHeight tests\n------------------");
        heightTest(6);

        out.println("\nDistance tests\n------------------");
        distanceTest(5, 5, 0);
        distanceTest(1, 16, 7);
        distanceTest(12, 22, 3);

        out.println("\nCeiling tests\n------------------");
        ceilingTest(12, 12);
        ceilingTest(15, 16);
        ceilingTest(7, 8);
        ceilingTest(30, null);

        out.println("\nLeaves count tests\n------------------");
        leavesCountTest(4);

        out.println("\nWidth count tests\n------------------");
        widthTest(3);

        out.println("\nDiameter tests\n------------------");
        diameterTest(8);

        out.println("\nRecovery(in & post) tests\n------------------");
        Integer []inorderData = {1, 5, 6, 8, 9, 12, 14, 16, 18, 19, 22};
        Integer []postorderData = {1, 6, 8, 5, 16, 18, 22, 19, 14, 12, 9};
        String preorder = "9 5 1 8 6 12 14 19 18 16 22 (size = 11)";
        recoverTest(inorderData, postorderData, preorder);

        out.println("\nSelect tests\n------------------");
        selectTest(0, 1);
        selectTest(2, 6);
        selectTest(7, 16);
        selectTest(30, null);

        out.println("\nIterator tests\n------------------");
        preorder = "9 5 1 8 6 12 14 19 18 16 22";
        iteratorTest(preorder);
    }

    static void containsTest(int value, boolean result) {
        boolean executionResult = tree.contains(value);
        String s = (executionResult == result) ? "OK" : "WRONG";
        out.format(rightPad, String.format("contains %d: %b", value, executionResult), s);
    }

    static void removeTest(int value, boolean result) {
        boolean executionResult = tree.remove(value);
        String s = (executionResult == result)
                ? "OK"
                : String.format("WRONG (%d still in the tree)", value);
        out.format(rightPad, String.format("remove %d: %b", value, executionResult), s);
    }

    static void lcaTest(int a, int b, int result) {
        int executionResult = tree.lca(a, b);
        String s = (executionResult == result) ? "OK" : "WRONG";
        out.format(rightPad, String.format("lca of %d & %d: %d", a, b, executionResult), s);
    }

    static void heightTest(int result) {
        int executionResult = tree.height();
        String s = (executionResult == result) ? "OK" : "WRONG";
        out.format(rightPad, String.format("tree height: %d", executionResult), s);
    }

    static void distanceTest(int a, int b, int result) {
        int executionResult = tree.distance(a, b);
        String s = (executionResult == result) ? "OK" : "WRONG";
        out.format(rightPad, String.format("Distance between %d & %d: %d", a, b, executionResult), s);
    }

    static void ceilingTest(int a, Integer result) {
        Integer executionResult = tree.ceiling(a);
        String s = (executionResult != null && executionResult == result) ? "OK" : "WRONG";
        if(executionResult == null && result == null)
            s = "OK";
        out.format(rightPad, String.format("Ceiling of %d: %d", a, executionResult), s);
    }

    static void leavesCountTest(int result) {
        int executionResult = tree.leavesCount();
        String s = (executionResult == result) ? "OK" : "WRONG";
        out.format(rightPad, String.format("Leaves count: %d", executionResult), s);
    }

    static void widthTest(int result) {
        int executionResult = tree.treeWidth();
        String s = (executionResult == result) ? "OK" : "WRONG";
        out.format(rightPad, String.format("Tree width: %d", executionResult), s);
    }

    static void diameterTest(int result) {
        int executionResult = tree.diameter();
        String s = (executionResult == result) ? "OK" : "WRONG";
        out.format(rightPad, String.format("Diameter: %d", executionResult), s);
    }

    static void recoverTest(Integer []inorder, Integer []postorder, String preorder) {
        MTBinarySearchTree<Integer> newTree = MTBinarySearchTree.recover(inorder, postorder);
        String text = newTree.toCustomString(MTBinarySearchTree.TreeStringType.PREORDER);
        String s = text.equals(preorder) ? "OK" : "WRONG";
        out.printf("Recoverd tree (pre-order): %s\n", text);
        out.format(rightPad, "", s);
    }

    static void selectTest(int a, Integer result) {
        Integer executionResult = tree.select(a);
        String s = (executionResult != null && executionResult == result) ? "OK" : "WRONG";
        if(executionResult == null && result == null)
            s = "OK";
        out.format(rightPad, String.format("%d-th smallest: %d", a, executionResult), s);
    }

    static void iteratorTest(String preorder) {
        StringBuilder sb = new StringBuilder();
        for(int elem : tree)
            sb.append(elem).append(" ");
        sb.deleteCharAt(sb.length()-1);
        String s = sb.toString().equals(preorder) ? "OK" : "WRONG";
        out.printf("Iterate result: %s\n", sb);
        out.format(rightPad, "", s);
    }
}
