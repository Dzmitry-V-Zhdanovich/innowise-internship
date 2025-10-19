package task4.minispring.core;

import task4.minispring.annotation.Component;
import task4.minispring.annotation.Scope;

@Component
@Scope("prototype")
public class UserRepository {
    public UserRepository() {
        System.out.println("UserRepository bean created.");
    }
}
