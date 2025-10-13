package task1.customlinkedlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private MyLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList<>();
    }

    @Test
    void testEmptyList() {
        assertEquals(0, list.size());
        assertThrows(IllegalStateException.class, () -> list.getFirst());
        assertThrows(IllegalStateException.class, () -> list.getLast());
    }

    @Test
    void testAddFirst() {
        list.addFirst("D");
        assertEquals(1, list.size());
        assertEquals("D", list.getFirst());
        assertEquals("D", list.getLast());
    }

    @Test
    void testAddLast() {
        list.addLast("A");
        list.addLast("B");
        assertEquals(2, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("B", list.getLast());
    }

    @Test
    void testAddAtIndex() {
        list.add(0, "A");
        list.add(1, "B");
        list.add(2, "C");
        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    void testRemoveFirst() {
        list.addLast("A");
        list.addLast("B");
        assertEquals("A", list.removeFirst());
        assertEquals("B", list.getFirst());
        assertEquals(1, list.size());
    }

    @Test
    void testRemoveLast() {
        list.addLast("A");
        list.addLast("B");
        assertEquals("B", list.removeLast());
        assertEquals("A", list.getLast());
        assertEquals(1, list.size());
    }

    @Test
    void testRemoveAtIndex() {
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        assertEquals("B", list.remove(1));
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    void testInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        list.addLast("D");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, "A"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    void testComplex() {
        list.addFirst("B");
        list.addFirst("A");
        list.addLast("C");

        assertEquals(3, list.size());
        assertEquals("A", list.getFirst());
        assertEquals("C", list.getLast());

        assertEquals("A", list.removeFirst());
        assertEquals("C", list.removeLast());
        assertEquals("B", list.remove(0));

        assertEquals(0, list.size());
        assertThrows(IllegalStateException.class, () -> list.removeFirst());
    }
}
