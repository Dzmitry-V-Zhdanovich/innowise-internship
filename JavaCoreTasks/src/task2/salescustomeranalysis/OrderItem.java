package task2.salescustomeranalysis;

import java.util.Objects;

public class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;

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
