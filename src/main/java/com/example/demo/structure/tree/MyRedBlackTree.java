package com.example.demo.structure.tree;

import lombok.Data;

import java.util.Comparator;

/**
 * @ClassName MyRedBlackTree
 * @Description: 红黑树
 *                 特点：
 *                      1、节点是红色或黑色
 *                      2、根节点和所有叶子节点是黑色（叶子是NIL节点，即叶子节点不存储数据）
 *                      3、每个红色节点的两个子节点都是黑色。（从每个叶子到根的所有路径上不能有两个连续的红色节点）
 *                      4、任一节点到其每个叶子的所有路径都包含相同数目的黑色节点
 * @Author zk   (改编自TreeMap)
 * @Date 2020/12/9
 **/
public class MyRedBlackTree<K,V> {

    /**
     * The comparator used to maintain order in this tree map, or
     * null if it uses the natural ordering of its keys.
     *
     * @serial
     */
    private final Comparator<? super K> comparator;

    private transient TreeNode<K,V> root;

    /**
     * The number of entries in the tree
     */
    private transient int size = 0;

    public MyRedBlackTree(K key, V value) {
        this.root = new TreeNode(key, value,null);
        comparator = null;
    }

    @Data
    private static class TreeNode<K,V> {

        private static final boolean RED   = false;
        private static final boolean BLACK = true;

        K key;
        V value;
        TreeNode<K,V> parent;
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        boolean color = BLACK;

        /**
         * 创建一个给定 key、value、parent的 黑色叶子节点
         */
        TreeNode(K key, V value, TreeNode<K,V> parent) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
            this.parent = parent;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        /**
         * 将当前与键关联的值替换为给定值
         *
         * @return 调用此方法之前与键关联的值
         */
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        static final boolean valEquals(Object o1, Object o2) {
            return (o1==null ? o2==null : o1.equals(o2));
        }

        public boolean equals(Object o) {
            if (!(o instanceof TreeNode))
                return false;
            TreeNode<?,?> e = (TreeNode<?,?>)o;

            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }

        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }


        @Override
        public String toString() {
            String s_color = color == true ? "BLACK" : "RED";
            return "{" +
                    "key=" + key +
                    ", value=" + value +
                    ", color=" + s_color +
                    '}';
        }
    }

    /**
     * ************************************************************************
     *
     * =============================== 平衡操作 ===============================
     * 插入时变换操作（新插入的节点颜色都是红色）：
     *         1、变颜色
     *            父亲和叔叔都是红色时
     *            把父亲和叔叔变为黑色，爷爷变成红色
     *            把爷爷设置为接下来要操作的节点
     *
     *         2、当父亲节点在左子树时
     *         2.1、左旋
     *            父亲是红色，叔叔是黑色，且当前节点在右子树，则以父亲节点进行左旋
     *         2.2、右旋
     *            父亲是红色，叔叔是黑色，且当且节点在左子树，则以爷爷节点进行右旋
     *            父亲变成黑色，爷爷变成红色
     *
     *         3、当父亲节点在右子树时
     *         3.1、右旋
     *            父亲是红色，叔叔是黑色，且当且节点在左子树，则以父亲节点进行右旋
     *         3.2、左旋
     *            父亲是红色，叔叔是黑色，且当前节点在右子树，则以爷爷节点进行左旋
     *            父亲变成黑色，爷爷变成红色
     *
     *
     * ************************************************************************
     */

    private static <K,V> boolean colorOf(TreeNode<K,V> p) {
        return (p == null ? TreeNode.BLACK : p.color);
    }

    private static <K,V> TreeNode<K,V> parentOf(TreeNode<K,V> p) {
        return (p == null ? null: p.parent);
    }

    private static <K,V> void setColor(TreeNode<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private static <K,V> TreeNode<K,V> leftOf(TreeNode<K,V> p) {
        return (p == null) ? null: p.left;
    }

    private static <K,V> TreeNode<K,V> rightOf(TreeNode<K,V> p) {
        return (p == null) ? null: p.right;
    }

    /**
     * 左旋
     *
     * @param p  p节点是 两个红色父子节点中的 父节点
     */
    private void rotateLeft(TreeNode<K,V> p) {
        if (p != null) {
            TreeNode<K,V> r = p.right;
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /**
     * 右旋
     *
     * @param p    p节点是 两个红色父子节点中的 父节点
     */
    private void rotateRight(TreeNode<K,V> p) {
        if (p != null) {
            TreeNode<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    public V put(K key, V value) {
        TreeNode<K,V> t = root;
        if (t == null) {
            compare(key, key); // type (and possibly null) check

            root = new TreeNode<>(key, value, null);
            size = 1;
            return null;
        }
        int cmp;
        TreeNode<K,V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        else {
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        TreeNode<K,V> e = new TreeNode<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        fixAfterInsertion(e);
        size++;
        return null;
    }

    public V remove(Object key) {
        TreeNode<K,V> p = getEntry(key);
        if (p == null)
            return null;

        V oldValue = p.value;
        deleteEntry(p);
        return oldValue;
    }

    final TreeNode<K,V> getEntry(Object key) {
        // Offload comparator-based version for sake of performance
        if (comparator != null)
            return getEntryUsingComparator(key);
        if (key == null)
            throw new NullPointerException();
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        TreeNode<K,V> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }
        return null;
    }

    final TreeNode<K,V> getEntryUsingComparator(Object key) {
        @SuppressWarnings("unchecked")
        K k = (K) key;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            TreeNode<K,V> p = root;
            while (p != null) {
                int cmp = cpr.compare(k, p.key);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
        }
        return null;
    }

    private void fixAfterInsertion(TreeNode<K,V> x) {
        x.color = TreeNode.RED;

        //父子节点都是红色，满足变换条件
        while (x != null && x != root && x.parent.color == TreeNode.RED) {
            //父亲节点在左子树
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                //y是叔叔节点
                TreeNode<K,V> y = rightOf(parentOf(parentOf(x)));
                //当父亲和叔叔都是红色的时候满足变色条件，
                //把父亲和叔叔变为黑色，爷爷变成红色
                //把爷爷设置为接下来要操作的节点
                if (colorOf(y) == TreeNode.RED) {
                    setColor(parentOf(x), TreeNode.BLACK);
                    setColor(y, TreeNode.BLACK);
                    setColor(parentOf(parentOf(x)), TreeNode.RED);
                    x = parentOf(parentOf(x));
                }
                //父亲是红色节点，而叔叔是黑色节点
                else {
                    //当前操作节点在右子树，则以父节点为操作节点进行左旋
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }

                    //左旋之后，满足右旋条件，进行右旋
                    setColor(parentOf(x), TreeNode.BLACK);
                    setColor(parentOf(parentOf(x)), TreeNode.RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            }
            //父亲节点在右子树
            else {
                //y是叔叔节点
                TreeNode<K,V> y = leftOf(parentOf(parentOf(x)));
                //当父亲和叔叔都是红色的时候满足变色条件，
                //把父亲和叔叔变为黑色，爷爷变成红色
                //把爷爷设置为接下来要操作的节点
                if (colorOf(y) == TreeNode.RED) {
                    setColor(parentOf(x), TreeNode.BLACK);
                    setColor(y, TreeNode.BLACK);
                    setColor(parentOf(parentOf(x)), TreeNode.RED);
                    x = parentOf(parentOf(x));
                }
                //父亲是红色节点，而叔叔是黑色节点
                else {
                    //当前操作节点在左子树，则以父节点为操作节点进行右旋
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    //右旋变色，父节点变成黑色，爷爷节点变成红色
                    setColor(parentOf(x), TreeNode.BLACK);
                    setColor(parentOf(parentOf(x)), TreeNode.RED);
                    //以爷爷为操作节点进行左旋
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = TreeNode.BLACK;
    }

    /**
     * Delete node p, and then rebalance the tree.
     */
    private void deleteEntry(TreeNode<K,V> p) {
        size--;

        // If strictly internal, copy successor's element to p and then make p
        // point to successor.
        if (p.left != null && p.right != null) {
            TreeNode<K,V> s = successor(p);
            p.key = s.key;
            p.value = s.value;
            p = s;
        } // p has 2 children

        // Start fixup at replacement node, if it exists.
        TreeNode<K,V> replacement = (p.left != null ? p.left : p.right);

        if (replacement != null) {
            // Link replacement to parent
            replacement.parent = p.parent;
            if (p.parent == null)
                root = replacement;
            else if (p == p.parent.left)
                p.parent.left  = replacement;
            else
                p.parent.right = replacement;

            // Null out links so they are OK to use by fixAfterDeletion.
            p.left = p.right = p.parent = null;

            // Fix replacement
            if (p.color == TreeNode.BLACK)
                fixAfterDeletion(replacement);
        } else if (p.parent == null) { // return if we are the only node.
            root = null;
        } else { //  No children. Use self as phantom replacement and unlink.
            if (p.color == TreeNode.BLACK)
                fixAfterDeletion(p);

            if (p.parent != null) {
                if (p == p.parent.left)
                    p.parent.left = null;
                else if (p == p.parent.right)
                    p.parent.right = null;
                p.parent = null;
            }
        }
    }

    /** From CLR */
    private void fixAfterDeletion(TreeNode<K,V> x) {
        while (x != root && colorOf(x) == TreeNode.BLACK) {
            if (x == leftOf(parentOf(x))) {
                TreeNode<K,V> sib = rightOf(parentOf(x));

                if (colorOf(sib) == TreeNode.RED) {
                    setColor(sib, TreeNode.BLACK);
                    setColor(parentOf(x), TreeNode.RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }

                if (colorOf(leftOf(sib))  == TreeNode.BLACK &&
                        colorOf(rightOf(sib)) == TreeNode.BLACK) {
                    setColor(sib, TreeNode.RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == TreeNode.BLACK) {
                        setColor(leftOf(sib), TreeNode.BLACK);
                        setColor(sib, TreeNode.RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), TreeNode.BLACK);
                    setColor(rightOf(sib), TreeNode.BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else { // symmetric
                TreeNode<K,V> sib = leftOf(parentOf(x));

                if (colorOf(sib) == TreeNode.RED) {
                    setColor(sib, TreeNode.BLACK);
                    setColor(parentOf(x), TreeNode.RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }

                if (colorOf(rightOf(sib)) == TreeNode.BLACK &&
                        colorOf(leftOf(sib)) == TreeNode.BLACK) {
                    setColor(sib, TreeNode.RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == TreeNode.BLACK) {
                        setColor(rightOf(sib), TreeNode.BLACK);
                        setColor(sib, TreeNode.RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), TreeNode.BLACK);
                    setColor(leftOf(sib), TreeNode.BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }

        setColor(x, TreeNode.BLACK);
    }
    public void print(TreeNode<K,V> node){
        System.out.println(node + " ");
    }

    //中序遍历
    public void in(TreeNode<K,V> root) {
        if(root != null) {
            if(root.getLeft() != null){
                in(root.getLeft());
            }
            print(root);
            if(root.getRight() != null){
                in(root.getRight());
            }
        }
    }

    /**
     * Returns the successor of the specified Entry, or null if no such.
     */
    static <K,V> TreeNode<K,V> successor(TreeNode<K,V> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            TreeNode<K,V> p = t.right;
            while (p.left != null)
                p = p.left;
            return p;
        } else {
            TreeNode<K,V> p = t.parent;
            TreeNode<K,V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }


    @SuppressWarnings("unchecked")
    final int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparator.compare((K)k1, (K)k2);
    }

    public static void main(String[] args) {
        MyRedBlackTree<Integer,String> myRedBlackTree = new MyRedBlackTree<Integer,String>(12,"d");
        myRedBlackTree.put(5,"a");
        myRedBlackTree.put(19,"b");
        myRedBlackTree.put(1,"c");
        myRedBlackTree.put(7,"e");
        myRedBlackTree.put(13,"f");
        myRedBlackTree.put(30,"f");
        myRedBlackTree.put(6,"f");
        myRedBlackTree.put(35,"f");

        myRedBlackTree.in(myRedBlackTree.root);

        /*System.out.println("删除一个节点：");
        myRedBlackTree.remove(4);
        myRedBlackTree.in(myRedBlackTree.root);*/
    }
}
