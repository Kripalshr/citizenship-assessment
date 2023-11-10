package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.MainApp;
import com.example.citizenshipassessment.model.CSVFileWriter;
import com.example.citizenshipassessment.model.Question;
import com.example.citizenshipassessment.model.QuizLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizController {

    @FXML
    public Label question, QuestionNo;

    @FXML
    public Button opt1, opt2, opt3, opt4, submit;

    @FXML
    public Label timer;
    private Timeline quizTimer;
    private int timeSeconds = 300;
    private Stage stage;
    private Scene scene;


    private static List<Question> questions;
    private static int counter = 0;
    private static int correct = 0;
    private static int wrong = 0;
    private int currentPosition = 0;



    private String loggedInUsername;

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }


    private List<String[]> selectedAnswers = new ArrayList<>();

    @FXML
    private void initialize() {
        loadQuestionsFromCSV();
        loadQuestions();
        startTimer();
        submit.setDisable(true); // Disable the submit button initially
    }
    private void loadQuestionsFromCSV() {
        questions = QuizLoader.loadQuestionsFromCSV("");
    }


    private void loadQuestions() {
        if (counter < questions.size()) {
            Question currentQuestion = questions.get(counter);
            QuestionNo.setText("Question " + (currentPosition + 1));
            question.setText(currentQuestion.getQuestionText());
            opt1.setText(currentQuestion.getOptions()[0]);
            opt2.setText(currentQuestion.getOptions()[1]);
            opt3.setText(currentQuestion.getOptions()[2]);
            opt4.setText(currentQuestion.getOptions()[3]);

            // Check if a selected answer matches any option and set the color
            String selectedAnswer = getSelectedAnswerForCurrentQuestion();
            setOptionColor(opt1, selectedAnswer, currentQuestion.getOptions()[0]);
            setOptionColor(opt2, selectedAnswer, currentQuestion.getOptions()[1]);
            setOptionColor(opt3, selectedAnswer, currentQuestion.getOptions()[2]);
            setOptionColor(opt4, selectedAnswer, currentQuestion.getOptions()[3]);
        }
    }
    private String getSelectedAnswerForCurrentQuestion() {
        return (counter < selectedAnswers.size()) ? selectedAnswers.get(counter)[1] : "";
    }

    private void setOptionColor(Button option, String selectedAnswer, String optionText) {
        if (!selectedAnswer.isEmpty() && selectedAnswer.equals(optionText)) {
            option.setTextFill(Color.GREEN);
            option.setFont(Font.font(null, FontWeight.BOLD, 18));
        } else {
            option.setTextFill(Color.WHITE);
            option.setFont(Font.font(null, FontWeight.BOLD, 18));
        }
    }

    @FXML
    public void previous(ActionEvent event) {
        if (currentPosition > 0) {
            currentPosition--;
            counter--;
            loadQuestions();
        }
    }

    @FXML
    public void next(ActionEvent event) {
        if (currentPosition < questions.size() - 1) {
            currentPosition++;
            counter++;
            loadQuestions();
        }
    }

    @FXML
    public void onSubmit(ActionEvent event) {
        storeSelectedAnswersInDatabase();
        openResultWindow(event);
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
            String[] answerData = new String[]{
                    currentQuestion.getQuestionText(),
                    selectedOption,
                    currentQuestion.getCorrectAnswer()
            };
            selectedAnswers.add(answerData);
            currentPosition++;
            loadQuestions();
        } else {
            submit.setDisable(false);
        }
    }

    private void openResultWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("result.fxml"));
            stage = new Stage();
            scene = new Scene(fxmlLoader.load());

            // Get the controller from the FXMLLoader
            ResultController resultController = fxmlLoader.getController();

            // Pass necessary data to the ResultController if needed
//            resultController.setLoggedInUsername(loggedInUsername);
//            resultController.setCorrectAnswersCount(correct);
//            resultController.setWrongAnswersCount(wrong);

            stage.setScene(scene);
            stage.show();

            // Save selected answers to a CSV file in resources
            CSVFileWriter.writeCSVFile("src/main/resources/selected_answers.csv", selectedAnswers);

            // Close the current quiz stage
            ((Stage) question.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeSelectedAnswersInDatabase() {
        // Modify these variables with your database connection details
        String url = AppConfig.DB_URL;
        String dbUsername = AppConfig.DB_USERNAME;
        String dbPassword = AppConfig.DB_PASSWORD;

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String sql = "INSERT INTO answers (username, answer_no_1, answer_no_2, answer_no_3, answer_no_4, answer_no_5, answer_no_6, answer_no_7, answer_no_8, answer_no_9, answer_no_10, answer_no_11, answer_no_12, answer_no_13, answer_no_14, answer_no_15, answer_no_16, answer_no_17, answer_no_18, answer_no_19, answer_no_20) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, loggedInUsername);

            // Set values for each answer (modify as needed)
            for (int i = 0; i < 20; i++) {
                String answerValue = (i < selectedAnswers.size()) ? selectedAnswers.get(i)[1] : ""; // Assuming the answer is stored in the second position of the answerData array
                statement.setString(i + 2, answerValue);
            }

            statement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // Handle the specific exception for duplicate entries
            String errorMessage = "Duplicate entry detected. Please provide a unique username.";
            showAlert("Duplicate Entry", errorMessage, Alert.AlertType.WARNING);

            // You can log the exception details or take other appropriate actions
        } catch (SQLException e) {
            // Handle other SQL exceptions
            String errorMessage = "An error occurred while processing your request.";
            showAlert("Error", errorMessage, Alert.AlertType.ERROR);
            e.printStackTrace(); // Log or handle the exception as needed
        } finally {
            // Close resources in the finally block
            // ...
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void startTimer() {
        quizTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeSeconds--;
            updateTimerLabel();
            if (timeSeconds <= 0) {
                quizTimer.stop();
                openResultWindow(null);
            }
        }));
        quizTimer.setCycleCount(Timeline.INDEFINITE);
        quizTimer.play();
    }

    private void stopTimer() {
        quizTimer.stop();
    }

    private void updateTimerLabel() {
        int minutes = timeSeconds / 60;
        int seconds = timeSeconds % 60;
        timer.setText(String.format("%02d:%02d", minutes, seconds));
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
