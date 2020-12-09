package com.example.demo.structure.tree;

import lombok.Data;

/**
 * @ClassName MyBinaryTree
 * @Description: 二叉树
 *                     A
 *                   /   \
 *                 B      E
 *                  \      \
 *                   C      F
 *                  /      /
 *                 D      G
 *                       / \
 *                      H   K
 *
 * @Author zk
 * @Date 2020/12/8
 **/
public class MyBinaryTree<E> {

    @Data
    private static class TreeNode<E> {
        E data;
        TreeNode<E> left;
        TreeNode<E> right;

        TreeNode(TreeNode<E> left, E element, TreeNode<E> right) {
            this.data = element;
            this.left = left;
            this.right = right;
        }
    }

    public void print(TreeNode<E> node){
        System.out.print(node.getData());
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
        TreeNode<Character> D = new TreeNode<Character>(null, 'D', null);
        TreeNode<Character> H = new TreeNode<Character>(null, 'H', null);
        TreeNode<Character> K = new TreeNode<Character>(null, 'K', null);
        TreeNode<Character> C = new TreeNode<Character>( D, 'C',null);
        TreeNode<Character> G = new TreeNode<Character>( H,'G', K);
        TreeNode<Character> B = new TreeNode<Character>( null,'B', C);
        TreeNode<Character> F = new TreeNode<Character>( G, 'F',null);
        TreeNode<Character> E = new TreeNode<Character>( null,'E', F);
        TreeNode<Character> A = new TreeNode<Character>( B, 'A',E);

        MyBinaryTree binaryTree = new MyBinaryTree();
        System.out.println("前序遍历");
        binaryTree.pre(A);
        System.out.println();
        System.out.println("中序遍历");
        binaryTree.in(A);
        System.out.println();
        System.out.println("后序遍历");
        binaryTree.post(A);
    }

}
