package com.test.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTests {

	@Test
	void contextLoads() {
	}

	// @Test
	// @FileParameters(value = "classpath:financial.csv", mapper = CsvWithHeaderMapper.class)
	// public void testLetterCount(String value, String letterCount) {
	// 	// assertEquals(letterCount, LetterCounter.countLetters(value));
	// }

    @ParameterizedTest
    @MethodSource("csvDataProvider")
    public void testWithCustomCsvDataProvider(String arg1, String arg2) {
        System.out.println("arg1 = " + arg1 + ", arg2 = " + arg2);
    }

    private static Stream<String[]> csvDataProvider() {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("D:/csv/mtname.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data.stream();
    }

}
