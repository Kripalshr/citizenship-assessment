package com.example.citizenshipassessment.model;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizLoader {

    public static List<Question> loadQuestionsFromCSV(String csvFilePath) {
        List<Question> questions = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader("/Users/kripal/Project Files/java/citizenship-assessment/src/main/resources/com/example/citizenshipassessment/questions.csv")).withSkipLines(1).build()) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if (line.length >= 6) {
                    String questionText = line[0];
                    String[] options = new String[4];
                    options[0] = line[1];
                    options[1] = line[2];
                    options[2] = line[3];
                    options[3] = line[4];
                    String correctAnswer = line[5];
                    questions.add(new Question(questionText, options, correctAnswer));
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
