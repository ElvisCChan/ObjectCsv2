package com.test.main;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ObjecTest {

    public static void main(String[] args) {
        Company company = new Company("ABC Inc.", 1000, List.of(
                new Employee("John", 25),
                new Employee("Jane", 30)
        ));

        List<String> fieldNames = getObjectFieldNames(company);

        for (String fieldName : fieldNames) {
            System.out.println(fieldName);
        }
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    private static List<String> getObjectFieldNames(Object object) {
        List<String> fieldNames = new ArrayList<>();

        Class<?> clazz = object.getClass();
        List<Field> fields = getAllFields(clazz);

        for (Field field : fields) {
            if (List.class.isAssignableFrom(field.getType())) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
                    Class<?> listType = (Class<?>) typeArguments[0];
                    fieldNames.addAll(getAllFields(listType).stream()
                            .map(Field::getName)
                            .map(fieldName -> field.getName() + "." + fieldName)
                            .collect(Collectors.toList()));
                }
            } else {
                fieldNames.add(field.getName());
            }
        }

        return fieldNames;
    }

    public static class Company {
        private String name;
        private int employeeCount;
        private List<Employee> employees;

        public Company(String name, int employeeCount, List<Employee> employees) {
            this.name = name;
            this.employeeCount = employeeCount;
            this.employees = employees;
        }

        // Getters and setters
    }

    public static class Employee {
        private String name;
        private int age;

        public Employee(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // Getters and setters
    }
}