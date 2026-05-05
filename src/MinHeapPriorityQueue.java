import java.util.Arrays;

 @SuppressWarnings("unchecked")
public class MinHeapPriorityQueue<E> {
    
    private static final int DEFAULT_CAPACITY = 10;
    private E[] data; // it can be an integer, a double, a long or a float type
    private int numElements;

    /**
     * A priority queue that organizes in natural ordering implemented using a min-heap.
     * It can take any element type.
     * We used Brett Jackson's code for priority queue as a reference.
     */
    public MinHeapPriorityQueue() {
        data = (E[]) (new Object[DEFAULT_CAPACITY]);
        numElements = 0;
    }
    /**
     * Inserts the given element into the priority queue.
     * Maintains the min heap property by bubbling up the element.
     * @param element   element to be insert
     * @return  true, if element was inserted correctly, otherwise false.
     */
    public boolean offer(E element) {
        if (element == null) {
            return false;
        }
        if (numElements >= data.length) {
            expandCapacity();
        }
        if (numElements == 0) {
            data[0] = element;
            numElements +=1;
            return true;
        }
        data[numElements] = element;        
        
        Comparable<? super E> key = (Comparable<? super E>) element;
        int index = numElements;
        int parentIndex = (int)Math.floor((double) (index - 1) /2);
       
        while (index > 0 && key.compareTo(data[parentIndex]) < 0) {
            // Using the natural ordering. Asking if the key is smaller than its parent, if so then swap
            data[index] = data[parentIndex]; // swapping parent's position
            data[parentIndex] = element;

            index = parentIndex;
            parentIndex = (int) Math.floor((double) (index - 1) /2);
        }
        numElements += 1;
        return true;
    }
    /**
     * Removes and returns the smallest element in the priority queue.
     * Maintains the min heap property by moving the last element and bubbling down 
     * until it is smaller than its children
     * @return  element in the head of array, the smallest in the queue
     */
    public E poll() {
        if (numElements == 0) {
            return null;
        }
        E temp = data[0];
        data[0] = data[numElements - 1];

        numElements -= 1;

        int i = 0; // indexing the parents
        while (i < numElements / 2) {
            // start with the left child as the smallest
            Comparable<? super E> smallest = (Comparable<? super E>) data[2 * i + 1];
            if (smallest.compareTo(data[2 * i + 2]) < 1) { // left child is the smallest
                data[i] = data[ 2 * i + 1];
            }
            i ++;
        }
        return temp;
    }
    /**
     * Gives the element with the most priority, i.e., the smallest in the min heap
     * @return  The head element of the array, i.e., at index 0.
     */
    public E peek() {
        return data[0]; 
    }
    /**
     * Helper method to get the size of the priority queue
     * @return  the number of elements in the array
     */
    public int size() {
        return numElements;
    }
    /**
     * Helper function. Changes the value of an element at the given index.
     * It doesn't maintain the min-heap property of priority queue. 
     * @param obj   new element
     * @param index index in the array to be modified
     */
    public void setValue(E obj, int index) {
        data[index] = obj;
    }

    public boolean isEmpty() {
        return (numElements == 0);
    }

    private void expandCapacity() {
        data = Arrays.copyOf(data, data.length * 2);
    }

    

}
