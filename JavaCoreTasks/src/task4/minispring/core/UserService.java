package task4.minispring.core;

import task4.minispring.annotation.Autowired;
import task4.minispring.annotation.Component;
import task4.minispring.annotation.Scope;

@Component
@Scope
public class UserService implements InitializingBean {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void afterPropertiesSet() {
        System.out.println("UserService initialised.");
    }
}
