package task4.minispring;

import task4.minispring.context.MiniApplicationContext;
import task4.minispring.core.UserService;

public class Application {
    public static void main(String[] args) {
        MiniApplicationContext context = new MiniApplicationContext("task4.minispring");
        UserService userService = context.getBean(UserService.class);
    }
}
