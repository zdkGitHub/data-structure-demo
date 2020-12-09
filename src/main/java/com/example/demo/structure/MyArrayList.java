package com.example.demo.structure;

import java.util.Arrays;

/**
 * @ClassName MyArrayList
 * @Description: TODO
 * @Author zk
 * @Date 2020/12/7
 **/
public class MyArrayList<E> {

    /**
     * 默认初始容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 空数组实例
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 空数组实例.
     * We distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
     * first element is added.
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * 存储ArrayList元素的数组缓冲区。
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer. Any
     * empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
     * will be expanded to DEFAULT_CAPACITY when the first element is added.
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * ArrayList的大小（它包含的元素数）
     */
    private int size;


    /**
     * 构造具有指定初始容量的空列表。
     *
     * @param  initialCapacity  the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *         is negative
     */
    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    /**
     * 构造一个初始容量为10的空列表。
     */
    public MyArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 将指定的元素追加到此列表的末尾。
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }

    /**
     * 在此列表的指定位置插入指定的元素。将当前位于该位置的元素（如果有）和任何后续元素向右移动（在其索引中添加一个元素）。
     *
     * @param index 要插入指定元素的索引
     * @param element 要插入的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);  // Increments modCount!!

        //1、jdk实现
        /*System.arraycopy(elementData, index, elementData, index + 1,
                size - index);*/
        //2、自己实现
        for(int i = size; i > index ; i --){
            elementData[i] = elementData[i - 1];	//把数据往后移一个
        }

        elementData[index] = element;
        size++;
    }

    // 位置的访问操作

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * 返回此列表中指定位置的元素。
     *
     * @param  index 要返回的元素的索引
     * @return 此列表中指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }

    /**
     * 将此列表中指定位置的元素替换为指定元素。
     *
     * @param index 要替换的元素的索引
     * @param element 要存储在指定位置的元素
     * @return 先前位于指定位置的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
        rangeCheck(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    /**
     * 删除此列表中指定位置的元素。
     * 向左移动任何后续元素（从其索引中减去一个）。
     *
     * @param index 要删除的元素的索引
     * @return 从列表中删除的元素
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        rangeCheck(index);

        E oldValue = elementData(index);

        //1、jdk实现
        /*int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);*/


        //2、自己实现
        for(int i = index; i < size ; i++){
            elementData[i] = elementData[i + 1];	//把数据往前移一个
        }

        elementData[--size] = null; // clear to let GC do its work
        return oldValue;
    }

    /**
     *  删除此列表中的所有元素。此调用返回后，列表将为空。
     */
    public void clear() {

        // clear to let GC do its work
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    @Override
    public String toString() {
        return "MyArrayList{" +
                "elementData=" + Arrays.toString(elementData) +
                ", size=" + size +
                '}';
    }

    /**
     * 检查给定索引是否在范围内。如果不是，则抛出适当的运行时异常。
     * 此方法不会*不*检查索引是否为负：它总是在数组访问之前使用，如果索引为负，则抛出ArrayIndexOutOfBoundsException。
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * add和addAll使用的rangeCheck版本
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * 构造IndexOutFoundsException详细信息消息。
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private void ensureExplicitCapacity(int minCapacity) {
        /*if (minCapacity - elementData.length > 0)
            throw new RuntimeException("数组越界，暂不支持扩容");*/

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * 要分配的数组的最大大小。
     * 一些vm在数组中保留一些header。
     * 尝试分配较大的阵列可能会导致OutOfMemoryError:请求的阵列大小超过VM限制
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 增加容量，以确保它至少可以容纳minimum capacity参数指定的元素数。
     *
     * @param minCapacity 期望的最小容量
     */
    private void grow(int minCapacity) {
        System.out.println("扩容......");
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:

        //1、JDK实现
        //elementData = Arrays.copyOf(elementData, newCapacity);

        //2、自己实现
        Object[] copy = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            copy[i] = elementData[i];
        }
        elementData = copy;

    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    public static void main(String[] args) {
        MyArrayList<Integer> arrayList = new MyArrayList<>();
        arrayList.add(new Integer("1"));
        arrayList.add(new Integer("2"));
        arrayList.add(new Integer("3"));
        arrayList.add(new Integer("4"));
        arrayList.add(new Integer("5"));

        //arrayList.add(4,78);
        System.out.println(arrayList.toString());

        //System.out.println(arrayList.get(-1));

        arrayList.remove(1);
        System.out.println(arrayList.toString());
    }
}
