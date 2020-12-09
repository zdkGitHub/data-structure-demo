package com.example.demo.structure;

/**
 * @ClassName MyLinkedList
 * @Description: 双向链表
 * @Author zk
 * @Date 2020/12/7
 **/
public class MyLinkedList<E> {
    transient int size = 0;

    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     *            (first.prev == null && first.item != null)
     */
    transient Node<E> first;

    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     *            (last.next == null && last.item != null)
     */
    transient Node<E> last;

    /**
     * 构造一个空列表
     */
    public MyLinkedList() {
    }

    /**
     * 将指定的元素追加到此列表的末尾。
     *
     * <p>此方法相当于 {@link #addLast}.
     *
     * @param e 要附加到此列表的元素
     * @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    /**
     * 在此列表的指定位置插入指定的元素。.
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (index == size)
            linkLast(element);
        else
            linkBefore(element, node(index));
    }

    /**
     * 位置的访问操作
     * 返回此列表中指定位置的元素.
     *
     * @param index 要返回的元素的索引
     * @return 此列表中指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }

    /**
     * 将此列表中指定位置的元素替换为指定元素.
     *
     * @param index 要替换的元素的索引
     * @param element 要存储在指定位置的元素
     * @return 先前位于指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        checkElementIndex(index);
        Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    /**
     * 删除此列表中指定位置的元素。向左移动任何后续元素（从其索引中减去一个）。
     * 返回从列表中删除的元素.
     *
     * @param index 要删除的元素的索引
     * @return 先前位于指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        checkElementIndex(index);
        return unlink(node(index));
    }

    /**
     * 删除此列表中的所有元素。此调用返回后，列表将为空.
     */
    public void clear() {
        // 清除节点之间的所有链接是“不必要的”，但是：
        //-如果丢弃的节点驻留在多个代中，则帮助分代GC
        //—即使存在可访问的迭代器，也确保释放内存
        for (Node<E> x = first; x != null; ) {
            Node<E> next = x.next;
            x.item = null;
            x.next = null;
            x.prev = null;
            x = next;
        }
        first = last = null;
        size = 0;
    }

    /**
     * 返回指定元素索引处的（非空）节点.
     */
    Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    /**
     * 链接e作为最后一个元素.
     */
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    /**
     * 在非空节点succ之前插入元素e.
     */
    void linkBefore(E e, Node<E> succ) {
        // assert succ != null;
        final Node<E> pred = succ.prev;
        final Node<E> newNode = new Node<>(pred, e, succ);
        succ.prev = newNode;
        if (pred == null)
            first = newNode;
        else
            pred.next = newNode;
        size++;
    }

    /**
     * 取消非空节点x的链接.
     */
    E unlink(Node<E> x) {
        // assert x != null;
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    @Override
    public String toString() {
        StringBuilder sb_items = new StringBuilder();
        Node current = first;
        while (current != null && current.next != null) {
            sb_items = sb_items.append(current.item.toString()).append("-->");
            current = current.next;
        }
        if(current != null) {
            sb_items = sb_items.append(current.item.toString());
        }

        return "MyLinkedList{" +
                "size=" + size +
                ",items=" + sb_items.toString() +
                '}';
    }

    /**
     * 指示参数是否是现有元素的索引.
     */
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }

    /**
     * 指示参数是迭代器或者add操作的有效位置的索引.
     */
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }

    /**
     * 构造IndexOutFoundsException详细信息消息。
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    public static void main(String[] args) {
        MyLinkedList<String> linkedList = new MyLinkedList();
        System.out.println(linkedList.toString());
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        System.out.println(linkedList.toString());
    }
}
