package com.example.demo.structure;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * @ClassName MyStack
 * @Description: 栈
 * @Author zk
 * @Date 2020/12/8
 **/
public class MyStack<E> extends MyVector<E> {

    /**
     * 创建空栈.
     */
    public MyStack() {

    }

    /**
     * 将item推送到栈顶
     *
     * @param   item   the item to be pushed onto this stack.
     * @return  the <code>item</code> argument.
     * @see     java.util.Vector#addElement
     */
    public E push(E item) {
        addElement(item);

        return item;
    }

    /**
     * 移除此栈顶的对象，并将该对象作为此函数的值返回.
     *
     * @return  此栈顶的对象.
     * @throws  EmptyStackException  如果此堆栈为空.
     */
    public synchronized E pop() {
        E       obj;
        int     len = size();

        obj = peek();
        removeElementAt(len - 1);

        return obj;
    }

    /**
     * 查看位于此堆栈顶部的对象，而不将其从堆栈中移除.
     *
     * @return  此栈顶的对象.
     * @throws  EmptyStackException  如果此堆栈为空.
     */
    public synchronized E peek() {
        int     len = size();

        if (len == 0)
            throw new EmptyStackException();
        return elementAt(len - 1);
    }

    /**
     * 判断此堆栈是否为空.
     */
    public boolean empty() {
        return size() == 0;
    }

    /**
     * 返回对象在此堆栈上从1开始的位置.栈顶对象的位置是1
     *
     * @param   o   查询的对象.
     * @return  从栈顶（栈顶的位置是1）到对象所在位置;
     *          返回<code>-1</code>表示对象不在堆栈上.
     */
    public synchronized int search(Object o) {
        int i = lastIndexOf(o);

        if (i >= 0) {
            return size() - i;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "MyStack{" +
                "elementData=" + Arrays.toString(elementData) +
                ", elementCount=" + elementCount +
                '}';
    }



    public static void main(String[] args) {
        MyStack<String> myStack = new MyStack<>();
        myStack.push("a");
        myStack.push("b");
        myStack.push("c");
        myStack.push("d");
        System.out.println(myStack);
        System.out.println(myStack.pop());
        System.out.println(myStack);
        System.out.println(myStack.peek());
        System.out.println(myStack);
    }

}
