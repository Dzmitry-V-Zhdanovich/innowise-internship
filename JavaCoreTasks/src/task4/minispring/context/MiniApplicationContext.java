package task4.minispring.context;

import task4.minispring.core.InitializingBean;
import task4.minispring.annotation.Autowired;
import task4.minispring.annotation.Component;
import task4.minispring.annotation.Scope;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MiniApplicationContext {
    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final String basePackage;

    public MiniApplicationContext(String basePackage) {
        this.basePackage = basePackage;
        initializeContext();
    }

    private void initializeContext() {
        // Сканируем пакет и находим классы с @Component
        Set<Class<?>> classesWithComponent = scanPackage(basePackage);
        // Создаем экземпляры бинов (синглтоны) и кладем в мапу
        for (Class<?> someClass : classesWithComponent) {
            if (someClass.isAnnotationPresent(Component.class)) {
                // Проверяем, является ли бин синглтоном (по умолчанию) или прототипом
                Scope scopeAnnotation = someClass.getAnnotation(Scope.class);
                String scope = scopeAnnotation != null ? scopeAnnotation.value() : "singleton";
                if ("singleton".equals(scope)) {
                    // Создаем бин и сохраняем его
                    Object instance = createBean(someClass);
                    beans.put(someClass, instance);
                }
                // Для прототипов мы не создаем экземпляр заранее, а будем создавать при каждом getBean
            }
        }
        // Внедряем зависимости в синглтоны
        for (Object bean : beans.values()) {
            injectDependency(bean);
        }
        // Вызываем afterPropertiesSet для синглтонов
        for (Object bean : beans.values()) {
            if (bean instanceof InitializingBean) {
                ((InitializingBean) bean).afterPropertiesSet();
            }
        }
    }

    private Set<Class<?>> scanPackage(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        // Получаем путь к пакету
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return classes;
        }
        File directory = new File(resource.getFile());
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    classes.addAll(scanPackage(packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    try {
                        Class<?> someClass = Class.forName(className);
                        if (someClass.isAnnotationPresent(Component.class)) {
                            classes.add(someClass);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return classes;
    }

    private Object createBean(Class<?> someClass) {
        try {
            return someClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean of type " + someClass.getName(), e);
        }
    }

    private void injectDependency(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                // Получаем тип поля (это класс зависимости)
                Class<?> fieldType = field.getType();
                // Ищем бин этого типа в контексте
                Object dependency = getBean(fieldType);
                if (dependency == null) {
                    throw new RuntimeException("No bean found for type " + fieldType);
                }
                field.setAccessible(true);
                try {
                    field.set(bean, dependency);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject dependency into field " + field.getName(), e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        // Проверяем, есть ли бин такого типа в мапе (для синглтонов)
        Object bean = beans.get(type);
        if (bean != null) {
            return (T) bean;
        }

        // Если бина нет, возможно, это прототип или еще не созданный бин?
        // Сначала проверим, есть ли класс с @Component и с областью prototype
        if (type.isAnnotationPresent(Component.class)) {
            Scope scopeAnnotation = type.getAnnotation(Scope.class);
            String scope = scopeAnnotation != null ? scopeAnnotation.value() : "singleton";
            if ("prototype".equals(scope)) {
                // Создаем новый экземпляр
                Object instance = createBean(type);
                // Внедряем зависимости в этот экземпляр
                injectDependency(instance);
                // Вызываем afterPropertiesSet, если реализует интерфейс
                if (instance instanceof InitializingBean) {
                    ((InitializingBean) instance).afterPropertiesSet();
                }
                return (T) instance;
            }
        }

        // Если бин не найден и не prototype, вернем null или выбросим исключение
        return null;
    }
}
