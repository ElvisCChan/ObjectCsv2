package com.test.main;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SuperCsvTest2 {

    public static void main(String[] args) throws IOException {
        String csvFile = "output.csv";

        // Create a list of Person and Country objects
        Person2 person = new Person2();
        person.setName("John");
        person.setAge(30);

        Country2 country = new Country2();
        country.setName("United States");
        country.setLocation("North America");

        Combine combine = new Combine(person, country);

        

        // Create the CSV writer
        try (ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(new FileWriter(csvFile), CsvPreference.STANDARD_PREFERENCE)) {
            // Define the CSV header
            String[] header = {"person.name", "age", "name", "location"};

            // Write the header
            csvBeanWriter.writeHeader(header);

            // Write the row
            csvBeanWriter.write(combine, header);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Person2 {
        private String name;
        private int age;
    
        // Getters and setters
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Country2 {
        private String name;
        private String location;
    
        // Getters and setters
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Combine {
        private Person2 person;
        private Country2 country;
    
        // Getters and setters

    }

}

