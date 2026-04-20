public class Bag<Item> implements Iterable<Item> {
    
    private Node<Item> first;
    private int n;

    private static class Node<Item> {
        private Item item; 
        private Node<Item> next;
    }

    
}
