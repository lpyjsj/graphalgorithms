package Graphs;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author Matthias Bady
 */
public abstract class MyPriorityQueue<T> extends PriorityQueue<T> {

    public MyPriorityQueue(int initialCapacity, Comparator<? super T> comparator)
    {
        super(initialCapacity, comparator);
    }

    public abstract void decreaseKey(T elm);
}
