package com.test.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

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

    private static CellProcessor[] getCellProcessors() {
        // Define the cell processors for each field
        return new CellProcessor[] {
                new NotNull(), // name
                new NotNull(), // email
                new NotNull(), // age
                new Optional(), // age
                new Optional(), // age
                new Optional(), // age
        };
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