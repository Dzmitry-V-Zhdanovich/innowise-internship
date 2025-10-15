package task2.salescustomeranalysis;

import java.util.Objects;

public class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;

    public OrderItem(String productName, int quantity, double price, Category category) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(productName, orderItem.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productName);
    }
}
