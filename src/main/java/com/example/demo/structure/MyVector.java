package com.example.demo.structure;

/**
 * @ClassName MyVector
 * @Description: 向量
 * @Author zk
 * @Date 2020/12/8
 **/
public class MyVector<E> {

    /**
     * 最后一个元素后面的任何数组元素都是空的
     */
    protected Object[] elementData;

    /**
     * {@code elementData[0]} 到 {@code elementData[elementCount-1]}是实际的items。
     */
    protected int elementCount;

    /**
     * 当矢量的大小大于其容量时，其容量自动递增的量。如果容量增量小于或等于零，则向量的容量将在每次需要增长时加倍.
     *
     * @serial
     */
    protected int capacityIncrement;

    /**
     * 构造具有指定初始容量和容量增量的空向量.
     *
     * @param   initialCapacity     向量的初始容量
     * @param   capacityIncrement   向量溢出时容量增加的量
     * @throws IllegalArgumentException 如果指定的初始容量为负数
     */
    public MyVector(int initialCapacity, int capacityIncrement) {
        super();
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        this.elementData = new Object[initialCapacity];
        this.capacityIncrement = capacityIncrement;
    }

    /**
     * 构造具有指定初始容量且容量增量等于零的空向量.
     *
     * @param   initialCapacity   向量的初始容量
     * @throws IllegalArgumentException 如果指定的初始容量为负数
     */
    public MyVector(int initialCapacity) {
        this(initialCapacity, 0);
    }

    /**
     * 构造一个空向量，使其内部数据数组的大小为{@code10}，其标准容量增量为零.
     */
    public MyVector() {
        this(10);
    }


    /**
     * 返回此堆栈中指定元素最后一次出现的索引，如果此堆栈不包含元素，则返回-1.
     *
     * @param o 查询的对象
     * @return 从栈顶（栈顶的位置是1）到对象所在位置;
     *         返回<code>-1</code>表示对象不在堆栈上
     */
    public synchronized int lastIndexOf(Object o) {
        return lastIndexOf(o, elementCount-1);
    }

    /**
     * 返回此堆栈中指定元素最后一次出现的索引，如果此堆栈不包含元素，则返回-1.
     *
     * @param o 查询的对象
     * @param index 开始向后（向后是针对于栈来说的）搜索的索引
     * @return 此堆栈中指定元素最后一次出现的索引，如果此堆栈不包含元素，则返回-1.
     * @throws IndexOutOfBoundsException 如果指定的索引大于或等于此堆栈的当前大小
     */
    public synchronized int lastIndexOf(Object o, int index) {
        if (index >= elementCount)
            throw new IndexOutOfBoundsException(index + " >= "+ elementCount);

        if (o == null) {
            for (int i = index; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = index; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }


    public synchronized void addElement(E obj) {
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = obj;
    }

    /**
     * 删除指定索引处的元素.后面的元素向前移动
     *
     * <p>索引的值必须大于或等于0并且小于堆栈的当前大小.
     *
     * @param      index   要删除的对象的索引
     * @throws ArrayIndexOutOfBoundsException 如果索引超出范围
     *         ({@code index < 0 || index >= size()})
     */
    public synchronized void removeElementAt(int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " +
                    elementCount);
        }
        else if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        //1、jdk实现
        /*int j = elementCount - index - 1;
        if (j > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, j);
        }*/

        //2、自己实现
        for(int i = index; i < elementCount ; i++){
            elementData[i] = elementData[i + 1];	//把数据往前移一个
        }

        elementCount--;
        elementData[elementCount] = null; /* to let gc do its work */
    }


    /**
     * 返回指定索引处的元素.
     * @param      index   an index into this vector
     * @return     the component at the specified index
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     *         ({@code index < 0 || index >= size()})
     */
    public synchronized E elementAt(int index) {
        if (index >= elementCount) {
            throw new ArrayIndexOutOfBoundsException(index + " >= " + elementCount);
        }

        return elementData(index);
    }

    // 位置的访问操作

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }


    public synchronized void insertElementAt(E obj, int index) {
        if (index > elementCount) {
            throw new ArrayIndexOutOfBoundsException(index
                    + " > " + elementCount);
        }
        ensureCapacityHelper(elementCount + 1);


        //1、JDK实现
        System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
        //2、自己实现
        for(int i = index; i < elementCount ; i++){
            elementData[i] = elementData[i + 1];	//把数据往前移一个
        }

        elementData[index] = obj;
        elementCount++;
    }

    public synchronized boolean removeElement(Object obj) {
        int i = indexOf(obj);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    public int indexOf(Object o) {
        return indexOf(o, 0);
    }

    public synchronized int indexOf(Object o, int index) {
        if (o == null) {
            for (int i = index ; i < elementCount ; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = index ; i < elementCount ; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * 返回堆栈中的元素数量.
     *
     * @return  此堆栈中的元素数量
     */
    public synchronized int size() {
        return elementCount;
    }

    /**
     * 如有必要，增加此数组的容量，以确保它至少可以容纳由最小容量参数指定的组件数量.
     *
     * <p>如果当前数组的容量小于{@code minCapacity},
     * 则通过替换其内部数据阵列来增加其容量, 保存在 {@code elementData}
     * 新数组的大小将是旧的大小加上{@code capacityIncrement},
     * 除非{@code capacityIncrement} 小于或等于零, 在这种情况下，新容量将是旧容量的两倍;
     * 但如果这个新大小仍然小于 {@code minCapacity}, 那么新的容量将是 {@code minCapacity}.
     *
     * @param minCapacity 期望的最小容量
     */
    public synchronized void ensureCapacity(int minCapacity) {
        if (minCapacity > 0) {
            ensureCapacityHelper(minCapacity);
        }
    }

    /**
     * 这实现了ensureCapacity的非同步语义。
     *
     * 此类中的同步方法可以在内部调用此方法以确保容量，而不会产生额外的同步开销.
     *
     * @see #ensureCapacity(int)
     */
    private void ensureCapacityHelper(int minCapacity) {
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
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ?
                capacityIncrement : oldCapacity);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:

        //1、JDK实现
        //elementData = Arrays.copyOf(elementData, newCapacity);

        //2、自己实现
        Object[] copy = new Object[newCapacity];
        for (int i = 0; i < elementCount; i++) {
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

    public synchronized E get(int index) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        return elementData(index);
    }

    public synchronized E set(int index, E element) {
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    public synchronized boolean add(E e) {
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = e;
        return true;
    }

    public void add(int index, E element) {
        insertElementAt(element, index);
    }

    public boolean remove(Object o) {
        return removeElement(o);
    }
}
