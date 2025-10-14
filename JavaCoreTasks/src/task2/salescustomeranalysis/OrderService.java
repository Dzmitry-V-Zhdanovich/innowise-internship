package task2.salescustomeranalysis;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class OrderService {

    //List of unique cities
    public Set<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(Order::getCustomer)
                .map(Customer::getCity)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    //Total income for all completed orders
    public double getTotalIncome(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }

    //The most popular product
    public String getMostPopularProduct(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProductName, Collectors.summingInt(OrderItem::getQuantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Товаров нет.");
    }

    //Average check for successfully delivered orders
    public double getAverageCheck(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .mapToDouble(order -> order.getItems().stream()
                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum())
                .average()
                .orElse(0);
    }

    //Customers with more than 5 orders
    public List<Customer> getCustomersWithMoreThan5Orders(List<Order> orders) {
        Map<Customer, Long> countOrders = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.counting()));

        return countOrders.entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
