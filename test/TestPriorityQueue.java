import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestPriorityQueue {
    
    // 1. Test inserting into an empty heap
    @Test
    public void testOfferToEmptyHeap() {
        MinHeapPriorityQueue<Integer> minHeap = new MinHeapPriorityQueue<>();
        minHeap.offer(6);

        assertEquals(1, minHeap.size());
        assertEquals(6, minHeap.peek());
    }
    // 2. Test inserting a smaller element than the root & duplicate insertion
    @Test 
    public void testKeepsMinHeap() {
        MinHeapPriorityQueue<Integer> minHeap = new MinHeapPriorityQueue<Integer>();
        minHeap.offer(10);
        minHeap.offer(12);
        minHeap.offer(15);
        minHeap.offer(2);

        assertEquals(2, minHeap.peek());
        assertEquals(4, minHeap.size());
        assertEquals(true, minHeap.offer(2));
    }
    // 3. Test inserting a null element
    @Test
    public void testNullOffer() {
        MinHeapPriorityQueue<Integer> minHeap = new MinHeapPriorityQueue<Integer>();
        minHeap.offer(4);
       
        assertEquals(false, minHeap.offer(null));
        assertEquals(1, minHeap.size());
    }
    // 4. Test deletion and priority queue property and repetitive poll calls
    @Test
    public void testRemovingRoot() {
        MinHeapPriorityQueue<Integer> minHeap = new MinHeapPriorityQueue<Integer>();
        minHeap.offer(4);
        minHeap.offer(29);
        minHeap.offer(10);
        minHeap.offer(6);

        assertEquals(4, minHeap.poll());
        assertEquals(6, minHeap.peek());
        assertEquals(3, minHeap.size());

        assertEquals(6, minHeap.poll());
    }
    // 5. Test deleting from PQ of a single element
    @Test
    public void testDeleteSingleElement() {
        MinHeapPriorityQueue<Integer> minHeap = new MinHeapPriorityQueue<Integer>();
        minHeap.offer(5);

        assertEquals(5, minHeap.poll());
        assertEquals(0, minHeap.size());
    }
}
