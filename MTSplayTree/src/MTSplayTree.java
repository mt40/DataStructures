/**
 * Created by mt on 2/9/2016.
 */
public class MTSplayTree<T extends Comparable<T>> {
    Node<T> root;

    public void add(T key) {
        Node<T> newNode = new Node<T>(key);
        if(root == null) {
            root = newNode;
            return;
        }

        root = splay(root, key); // Bring the closest node (in terms of value) to root

        /**
         * Important note: we must have a consistent behavior in any tree:
         * if key >= node: key is on the right
         * else: key is on the left
         */
        if(root.val.compareTo(key) >= 0) { // Root should be on the right
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        }
        else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root =  newNode; // Please don't forget this!
    }

    public Node<T> get(T key) {
        root = splay(root, key);

        if(root.val.compareTo(key) != 0)
            return null;
        return root;
    }

    public boolean remove(T key) {
        root = splay(root, key);
        if(root != null && root.val.compareTo(key) == 0) {
            remove(root, key);
            return true;
        }
        return false;
    }

    /**
     * Remove in Binary Tree fashion
     */
    private void remove(Node<T> cur, T key) {
        if(cur == null) return;
        // Has 0 or 1 child
        if(cur.val.compareTo(key) == 0
                && (cur.left == null || cur.right == null)) {
            Node p = parent(root, cur);
            if(p == null) { // this is root
                root = (cur.left == null) ? cur.right : cur.left;
                return;
            }
            if(p.left == cur) p.left = (cur.left == null) ? cur.right : cur.left;
            else p.right = (cur.left == null) ? cur.right : cur.left;
            return;
        }
        // Has 2 children
        else if(cur.val.compareTo(key) == 0) {
            Node<T> newRoot = subtreeMin(cur.right);
            copy(newRoot, cur);
            remove(cur.right, newRoot.val);
            return;
        }

        if(cur.val.compareTo(key) > 0)
            remove(cur.left, key);
        else
            remove(cur.right, key);
    }

    private void copy(Node<T> src, Node<T> des) {
        des.val = src.val;
    }

    private Node<T> subtreeMin(Node<T> cur) {
        if(cur.left == null)
            return cur;
        return subtreeMin(cur.left);
    }

    private Node<T> parent(Node<T> cur, Node<T> target) {
        if(cur == null)
            return null;
        if(cur.left == target || cur.right == target)
            return cur;
        if(cur.val.compareTo(target.val) > 0)
            return parent(cur.left, target);
        return parent(cur.right, target);
    }

    private Node<T> splay(Node<T> cur, T key) {
        if(cur == null || cur.val.equals(key))
            return cur;
        if(cur.val.compareTo(key) > 0) { // Key is on the left
            if(cur.left == null)
                return cur;
            // zig-zig: left-left case
            if(cur.left.val.compareTo(key) > 0) {
                cur.left.left = splay(cur.left.left, key); // Recursively bring the key to left-left
                if(cur.left.left != null)
                    cur.left = rotateRight(cur.left);
            }
            // zig-zag: left-right case
            else if(cur.left.val.compareTo(key) < 0) {
                cur.left.right = splay(cur.left.right, key);
                if(cur.left.right != null)
                    cur.left = rotateLeft(cur.left);
            }
            return rotateRight(cur);
        }
        else { // Key is on the right. Do the opposite of above
            if(cur.right == null)
                return cur;
            // zag-zig: right-left case
            if(cur.right.val.compareTo(key) > 0) {
                cur.right.left = splay(cur.right.left, key);
                if(cur.right.left != null)
                    cur.right = rotateRight(cur.right);
            }
            // zag-zag: right-right case
            else if(cur.right.val.compareTo(key) < 0) {
                cur.right.right = splay(cur.right.right, key);
                if(cur.right.right != null)
                    cur.right = rotateLeft(cur.right);
            }
            return rotateLeft(cur);
        }
    }

    private Node<T> rotateRight(Node<T> x) {
        Node<T> y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node<T> rotateLeft(Node<T> x) {
        Node<T> y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    @Override
    public String toString() {
        return toString(root).replaceAll("\\s+", " ");
    }

    public String inorderString() {
        return inorderString(root).replaceAll("\\s+", " ");
    }

    private String inorderString(Node cur) {
        if (cur == null)
            return "";
        return String.format("%s %s %s", inorderString(cur.left), cur, inorderString(cur.right));
    }

    private String toString(Node cur) {
        if (cur == null)
            return "";
        return String.format("%s %s %s", cur, toString(cur.left), toString(cur.right));
    }

    class Node<E> {
        E val;
        Node<E> left, right;

        public Node(E val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val.toString();
        }
    }
}
