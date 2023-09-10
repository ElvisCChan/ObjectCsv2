// package com.test.main;
// import java.io.FileWriter;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// import org.supercsv.cellprocessor.CellProcessorAdaptor;
// import org.supercsv.cellprocessor.ift.CellProcessor;
// import org.supercsv.cellprocessor.ift.StringCellProcessor;
// import org.supercsv.io.CsvBeanWriter;
// import org.supercsv.io.ICsvBeanWriter;
// import org.supercsv.prefs.CsvPreference;
// import org.supercsv.util.CsvContext;

// public class CsvWriterExample {

//     public static void main(String[] args) throws IOException {
//         String csvFile = "output.csv";

//         // Create a list of objects with nested properties
//         List<Combine> combines = new ArrayList<>();
//         combines.add(new Combine(new Person("John", 30)));

//         // Define the CSV header
//         String[] header = {"Person Age"};

//         // Define the name mapping
//         String[] nameMapping = {"person.age"};

//         // Define the cell processor for the nested property
//         StringCellProcessor nestedProcessor = new NestedCellProcessor(new StringCellProcessor() {
//             @Override
//             public <T> T execute(Object value, CsvContext context) {
//                 return next.execute(((Person) value).getAge(), context);
//             }
//         });

//         // Create the CSV writer
//         try (ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(new FileWriter(csvFile), CsvPreference.STANDARD_PREFERENCE)) {
//             // Write the header
//             csvBeanWriter.writeHeader(header);

//             // Write the objects to the CSV file
//             for (Combine combine : combines) {
//                 csvBeanWriter.write(combine, nameMapping, new CellProcessor[]{nestedProcessor});
//             }
//         }
//     }
// }

// class Combine {
//     private Person person;

//     public Combine(Person person) {
//         this.person = person;
//     }

//     // Getter and setter for person
//     public Person getPerson() {
//         return person;
//     }

//     public void setPerson(Person person) {
//         this.person = person;
//     }
// }

// class Person {
//     private String name;
//     private int age;

//     public Person(String name, int age) {
//         this.name = name;
//         this.age = age;
//     }

//     // Getters and setters for name and age
//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public int getAge() {
//         return age;
//     }

//     public void setAge(int age) {
//         this.age = age;
//     }
// }

// class NestedCellProcessor extends CellProcessorAdaptor implements StringCellProcessor {
//     public NestedCellProcessor(StringCellProcessor next) {
//         super(next);
//     }
// }