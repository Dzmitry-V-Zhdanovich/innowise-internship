package task2.salescustomeranalysis;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    OrderService service = new OrderService();

    Customer customer1 = new Customer("1", "Dima", "minsk-dima@mail.ru",
            LocalDateTime.now(), 40, "Minsk");
    Customer customer2 = new Customer("2", "Bob", "123@mail.ru",
            LocalDateTime.now(), 35, "Moscow");
    Customer customer3 = new Customer("3", "John", "135@gmail.com",
            LocalDateTime.now(), 30, "London");

    OrderItem phone = new OrderItem("Phone", 7, 125.0, Category.ELECTRONICS);
    OrderItem laptop = new OrderItem("Laptop", 5, 270.0, Category.ELECTRONICS);
    OrderItem book = new OrderItem("Book", 3, 7.0, Category.BOOKS);

    @Test
    void testGetUniqueCities() {
        List<Order> orders = Arrays.asList(
                new Order("1", LocalDateTime.now(), customer1, List.of(laptop), OrderStatus.DELIVERED),
                new Order("2", LocalDateTime.now(), customer2, List.of(book), OrderStatus.PROCESSING)
        );

        assertEquals(2, service.getUniqueCities(orders).size());
        assertTrue(service.getUniqueCities(orders).containsAll(Arrays.asList("Minsk", "Moscow")));
    }

    @Test
    void testGetTotalIncome() {
        List<Order> orders = List.of(
                new Order("1", LocalDateTime.now(), customer3,
                        Arrays.asList(laptop, book), OrderStatus.DELIVERED)
        );

        double testIncome = 5 * 270.0 + 3 * 7.0;
        assertEquals(testIncome, service.getTotalIncome(orders));
    }

    @Test
    void testGetMostPopularProduct() {
        List<Order> orders = Arrays.asList(
                new Order("1", LocalDateTime.now(), customer1,
                        Arrays.asList(book, laptop), OrderStatus.DELIVERED),
                new Order("2", LocalDateTime.now(), customer2,
                        Arrays.asList(phone, book), OrderStatus.DELIVERED)
        );

        assertEquals("Phone", service.getMostPopularProduct(orders));
    }

    @Test
    void testGetAverageCheck() {
        List<Order> orders = Arrays.asList(
                new Order("1", LocalDateTime.now(), customer1, List.of(laptop), OrderStatus.DELIVERED),
                new Order("2", LocalDateTime.now(), customer3, List.of(phone), OrderStatus.DELIVERED)
        );

        double testAverageCheck = (5 * 270.0 + 7 * 125.0) / 2;
        assertEquals(testAverageCheck, service.getAverageCheck(orders));
    }

    @Test
    void testGetCustomersWithMoreThan5Orders() {
        List<Order> orders = Arrays.asList(
                new Order("1", LocalDateTime.now(), customer1, List.of(laptop), OrderStatus.DELIVERED),
                new Order("2", LocalDateTime.now(), customer1, List.of(book), OrderStatus.DELIVERED),
                new Order("3", LocalDateTime.now(), customer1, List.of(phone), OrderStatus.DELIVERED),
                new Order("4", LocalDateTime.now(), customer1, List.of(laptop), OrderStatus.DELIVERED),
                new Order("5", LocalDateTime.now(), customer1, List.of(book), OrderStatus.DELIVERED),
                new Order("6", LocalDateTime.now(), customer1, List.of(phone), OrderStatus.DELIVERED),
                new Order("7", LocalDateTime.now(), customer2, List.of(laptop), OrderStatus.DELIVERED)
        );

        assertEquals(1, service.getCustomersWithMoreThan5Orders(orders).size());
        assertTrue(service.getCustomersWithMoreThan5Orders(orders).contains(customer1));
    }
}
