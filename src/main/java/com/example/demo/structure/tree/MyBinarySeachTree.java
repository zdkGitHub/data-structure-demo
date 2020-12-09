package com.example.demo.structure.tree;

import lombok.Data;

/**
 * @ClassName MyBinarySeachTree
 * @Description: 二叉查找树、二叉排序树
 * @Author zk
 * @Date 2020/12/8
 **/
public class MyBinarySeachTree<E extends Comparable<E>> {
    @Data
    private static class TreeNode<E> {
        E data;
        TreeNode<E> left;
        TreeNode<E> right;

        TreeNode(E element) {
            this.data = element;
            this.left = null;
            this.right = null;
        }
    }

    //插入的时候每次都是和根结点比较。一直要找到它应该插入的位置。
    //肯定会插在叶子结点。那么其实大家可以看到 插入其实就是查找。 默认root不会为空

    public void insert(TreeNode<E> rootNode,E element) {
        if(rootNode != null) {
            if(rootNode.data.compareTo(element) == -1) {	//根节点小 我们要放到右边
                if(rootNode.right == null) {
                    rootNode.right = new TreeNode(element);
                }else {
                    insert(rootNode.right, element);
                }
            }else {
                if(rootNode.left == null) {
                    rootNode.left = new TreeNode(element);
                }else {
                    insert(rootNode.left, element);
                }
            }
        }

    }

    public boolean isHave(TreeNode<E> rootNode,E element) {
        if(rootNode != null) {
            if(rootNode.data.compareTo(element) == -1) {
                return isHave(rootNode.right, element);
            }else if(rootNode.data.compareTo(element) == 1) {
                return isHave(rootNode.left, element);
            }else {
                return true;
            }
        }
        return false;
    }

    public void print(TreeNode<E> node){
        System.out.print(node.getData() + " ");
    }

    //前序遍历
    public void pre(TreeNode<E> root) {
        if(root != null) {
            print(root);
            if(root.getLeft() != null){
                pre(root.getLeft());
            }
            if(root.getRight() != null){
                pre(root.getRight());
            }
        }
    }

    //中序遍历
    public void in(TreeNode<E> root) {
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

    //后序遍历
    public void post(TreeNode<E> root) {
        if(root != null) {
            if(root.getLeft() != null){
                post(root.getLeft());
            }
            if(root.getRight() != null){
                post(root.getRight());
            }
            print(root);
        }
    }

    public static void main(String[] args) {
        MyBinarySeachTree<Integer> myBinarySeachTree = new MyBinarySeachTree<Integer>();

        TreeNode<Integer> rootNode = new TreeNode<Integer>(50);
        myBinarySeachTree.insert(rootNode,20);
        myBinarySeachTree.insert(rootNode,11);
        myBinarySeachTree.insert(rootNode,43);
        myBinarySeachTree.insert(rootNode,22);
        myBinarySeachTree.insert(rootNode,2);
        myBinarySeachTree.insert(rootNode,23);
        myBinarySeachTree.insert(rootNode,38);
        myBinarySeachTree.insert(rootNode,19);

        System.out.println("中序遍历：");
        myBinarySeachTree.in(rootNode);

        System.out.println();
        System.out.println(myBinarySeachTree.isHave(rootNode,23));
        System.out.println(myBinarySeachTree.isHave(rootNode,12));
    }
}
