/**
 * Created by mt on 2/9/2016.
 */
public class main {
    public static void main(String...args) {
        int []a = {1, 5, 10, 4, 9, 0, 5, 5, 20};
        MTSplayTree<Integer> tree = new MTSplayTree<>();

        for(int ai : a)
            tree.add(ai);

        System.out.printf("Tree: %s\n\n", tree);

        // Search for an existing element
        System.out.printf("Search for %d: %s\n", 5, tree.get(5));
        System.out.printf("Tree: %s (searched element is at the top)\n", tree);

        // Search for an un-existing element
        System.out.printf("Search for %d: %s\n", 8, tree.get(8));
        System.out.printf("Tree: %s (closest result is at the top)\n", tree);

        // Remove an existing element
        System.out.printf("Remove %d: %b\n", 5, tree.remove(5));
        System.out.printf("Tree: %s\n", tree);

        // Remove an un-existing element
        System.out.printf("Remove %d: %b\n", 15, tree.remove(15));
        System.out.printf("Tree: %s\n", tree);

        System.out.println("\nSorted order");
        System.out.printf("Tree (in-order): %s\n", tree.inorderString());
    }
}
