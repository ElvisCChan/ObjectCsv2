package com.test.main;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SuperCsvTest {
    static FileWriter fileWriter;
    static CSVWriter csvWriter;

    public static void main(String[] args) throws IOException {
        fileWriter = new FileWriter("output.csv");
        csvWriter = new CSVWriter(fileWriter);

        List<Person> persons = new ArrayList<>();
        persons.add(new Person("John", 25, "john@example.com",
                List.of(Country.builder().name("India").location("Asia").build(),
                        Country.builder().name("USA").location("North America").build())));
        persons.add(new Person("Jane", 30, "jane@example.com",
                List.of(Country.builder().name("India").location("Asia").build(),
                        Country.builder().name("USA").location("North America").build())));

    

        List<String> fieldNames = ObjectUtils.getObjectFieldNames(Person.builder().build());
        String[] header = fieldNames.toArray(new String[fieldNames.size()]);
        csvWriter.writeNext(header);
        writeObjectListToCsv(persons);

        csvWriter.close();
    }

    private static void writeToCsv(Person person, Country country) throws IOException {

        String[] values = { String.valueOf(person.getName()), String.valueOf(person.getAge()),
                String.valueOf(person.getAge()), String.valueOf(country.getName()), String.valueOf(country.getName()),
                String.valueOf(country.getClient()) };

        csvWriter.writeNext(values);
        // csvWriter.close();

        System.out.println("CSV file written successfully.");
    }

    private static void writeObjectListToCsv(List<Person> persons) throws IOException {
        for (Person person : persons) {
            List<Country> countries = person.getCountry();
            for (Country country : countries) {
                writeToCsv(person, country);
            }

        }

    }

    public String toCommaSeparatedString(Object object) {
        List<String> fieldValues = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            // field.setAccessible(true);
            try {
                Object value = field.get(this);
                fieldValues.add(String.valueOf(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return String.join(",", fieldValues);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Person {
        private String name;
        private int age;
        private String email;
        private List<Country> country;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Country {
        private String name;
        private String location;
        private String client;

    }
}