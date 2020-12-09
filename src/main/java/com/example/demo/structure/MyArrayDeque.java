package com.example.demo.structure;

import java.util.Arrays;

/**
 * @ClassName MyArrayDeque
 * @Description: TODO
 * @Author zk
 * @Date 2020/12/8
 **/
public class MyArrayDeque<E> {

    /**
     * 存储deque元素的数组。
     * deque的容量是这个数组的长度，它总是2的幂次方。
     * 数组永远不允许变满，除非是在addX方法中临时变满（请参见doubleCapacity），
     * 在变满后立即调整大小（请参见doubleCapacity），
     * 这样就避免了头和尾缠绕在一起使其相等。
     * 我们还保证所有不包含deque元素的数组单元都是空的
     */
    transient Object[] elements; // 非私有以简化嵌套类访问

    /**
     * deque开头的元素的索引（它是通过remove（）或pop（）删除的元素）；
     * 如果deque为空，则是一个等于tail的任意数字.
     */
    transient int head;

    /**
     * 下一个元素将被添加到deque尾部的索引（通过addLast（E）、add（E）或push（E））.
     */
    transient int tail;

    /**
     * 构造一个空数组deque，其初始容量足以容纳16个元素.
     */
    public MyArrayDeque() {
        elements = new Object[16];
    }

    /**
     * 构造一个空数组deque，其初始容量足以容纳指定数量的元素.
     *
     * @param numElements  deque初始容量
     */
    public MyArrayDeque(int numElements) {
        allocateElements(numElements);
    }

    /**
     * 分配空数组以保存给定数量的元素.
     *
     * @param numElements  要保存的元素数
     */
    private void allocateElements(int numElements) {
        elements = new Object[calculateSize(numElements)];
    }

    /**
     * 用于新创建的deque的最小容量。
     * 必须是2的幂.
     */
    private static final int MIN_INITIAL_CAPACITY = 8;

    // ******  数组分配和调整大小公用程序 ******

    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        // 找到两个元素的最佳幂.
        // 测试“<=”，因为数组未保存满.
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // 太多的元素，必须后退
                initialCapacity >>>= 1;// 祝你好运分配2^30个元素
        }
        return initialCapacity;
    }

    /**
     * 在这个deque的末尾插入指定的元素
     *
     * 如果可以在不违反容量限制的情况下立即将指定的元素插入到这个deque表示的队列中（换句话说，在这个deque的尾部），
     * 成功时返回{@code true}，如果当前没有可用的空间，则返回{@code false}。
     * 当使用容量受限的deque时，此方法通常优于{@link#add}方法，
     * 后者插入元素失败会引发异常.
     *
     * <p>此方法相当于 {@link #offerLast}.
     *
     * @param e 要添加的元素
     * @return {@code true} 如果元素被添加到这个deque中,
     *         否则 {@code false}
     * @throws NullPointerException 如果指定的元素为null
     */
    public boolean offer(E e) {
        return offerLast(e);
    }


    /**
     * 检索并删除队列头（换句话说，deque的第一个元素），
     * 如果这个deque为空，则返回{@code null}.
     *
     * <p>此方法相当于 {@link #pollFirst}.
     *
     * @return 队列头，或者{@code null}（如果这个deque为空）
     */
    public E poll() {
        return pollFirst();
    }


    /**
     * 检索但不删除队列头，如果此deque为空，则返回{@code null}.
     *
     * <p>此方法相当于 {@link #peekFirst}.
     *
     * @return 队列头，或者{@code null}（如果这个deque为空）
     */
    public E peek() {
        return peekFirst();
    }

    @SuppressWarnings("unchecked")
    public E peekFirst() {
        // elements[head] is null if deque empty
        return (E) elements[head];
    }

    public E pollFirst() {
        int h = head;
        @SuppressWarnings("unchecked")
        E result = (E) elements[h];
        // 如果deque为空，则元素为null
        if (result == null)
            return null;
        elements[h] = null;     // Must null out slot
        head = (h + 1) & (elements.length - 1);
        return result;
    }

    /**
     * 在这个deque的末尾插入指定的元素.
     *
     * @param e 要添加的元素
     * @return {@code true} (as specified by {@link Deque#offerLast})
     * @throws NullPointerException 如果指定的元素为null
     */
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * 在这个deque的末尾插入指定的元素.
     *
     * <p>此方法相当于 {@link #add}.
     *
     * @param e 要添加的元素
     * @throws NullPointerException 如果指定的元素为null
     */
    public void addLast(E e) {
        if (e == null)
            throw new NullPointerException();
        elements[tail] = e;
        if ( (tail = (tail + 1) & (elements.length - 1)) == head)
            doubleCapacity();
    }


    /**
     * 把这个deque的容量翻一番.
     * 当满的时候才调用
     * 也就是说，当头和尾相等的时候.
     */
    private void doubleCapacity() {
        System.out.println("扩容......head:" + head + ",tail:" + tail);
        assert head == tail;
        int p = head;
        int n = elements.length;
        int r = n - p; // p右侧元素数
        int newCapacity = n << 1;
        if (newCapacity < 0)
            throw new IllegalStateException("Sorry, deque too big");
        Object[] a = new Object[newCapacity];
        System.arraycopy(elements, p, a, 0, r);
        System.arraycopy(elements, 0, a, r, p);
        elements = a;
        head = 0;
        tail = n;
    }

    @Override
    public String toString() {
        return "MyArrayDeque{" +
                "elements=" + Arrays.toString(elements) +
                ", head=" + head +
                ", tail=" + tail +
                '}';
    }

    public static void main(String[] args) {
        MyArrayDeque<String> myArrayDeque = new MyArrayDeque<>();
        myArrayDeque.offer("a");
        myArrayDeque.offer("b");
        myArrayDeque.offer("c");
        myArrayDeque.offer("d");
        myArrayDeque.offer("e");

        System.out.println(myArrayDeque.toString());

        System.out.println(myArrayDeque.peek());

        System.out.println(myArrayDeque.poll());

        System.out.println(myArrayDeque.toString());

        for (int i = 0; i < 12; i++) {
            myArrayDeque.offer(String.valueOf(i));
        }

        System.out.println(myArrayDeque.toString());
    }
}
