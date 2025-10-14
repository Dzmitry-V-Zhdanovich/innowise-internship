package task2.salescustomeranalysis;

import java.time.LocalDateTime;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private LocalDateTime registeredAt;
    private int age;
    private String city;

    public String getCity() {
        return city;
    }
}
