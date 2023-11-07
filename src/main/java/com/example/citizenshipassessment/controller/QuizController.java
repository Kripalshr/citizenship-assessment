package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.MainApp;
import com.example.citizenshipassessment.model.CSVFileWriter;
import com.example.citizenshipassessment.model.Question;
import com.example.citizenshipassessment.model.QuizLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizController {

    @FXML
    public Label question;

    @FXML
    public Button opt1, opt2, opt3, opt4;
    private Stage stage;
    private Scene scene;

    private static List<Question> questions;
    private static int counter = 0;
    private static int correct = 0;
    private static int wrong = 0;

    private List<String[]> selectedAnswers = new ArrayList<>();

    @FXML
    private void initialize() {
        loadQuestionsFromCSV();
        loadQuestions();
    }

    private void loadQuestionsFromCSV() {
        questions = QuizLoader.loadQuestionsFromCSV("");
    }

    private void loadQuestions() {
        if (counter < questions.size()) {
            Question currentQuestion = questions.get(counter);
            question.setText(currentQuestion.getQuestionText());
            opt1.setText(currentQuestion.getOptions()[0]);
            opt2.setText(currentQuestion.getOptions()[1]);
            opt3.setText(currentQuestion.getOptions()[2]);
            opt4.setText(currentQuestion.getOptions()[3]);
        }
    }

    @FXML
    public void optionClicked(ActionEvent event) {
        String selectedOption = ((Button) event.getSource()).getText();
        if (counter < questions.size()) {
            Question currentQuestion = questions.get(counter);
            if (currentQuestion.isCorrectAnswer(selectedOption)) {
                correct++;
            } else {
                wrong++;
            }
            counter++;

            // Store selected answer in the list
            String[] answerData = new String[] {
                    currentQuestion.getQuestionText(),
                    selectedOption,
                    currentQuestion.getCorrectAnswer()
            };
            selectedAnswers.add(answerData);
            loadQuestions();
        }
        else  {
            openResultWindow(event);

        }
    }

    private void openResultWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("result.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save selected answers to a CSV file in resources
        CSVFileWriter.writeCSVFile("src/main/resources/selected_answers.csv", selectedAnswers);
    }

    public static int getTotalQuestionsCount() {
        return questions.size();
    }

    public static int getCorrectAnswersCount() {
        return correct;
    }

    public static int getWrongAnswersCount() {
        return wrong;
    }
}
