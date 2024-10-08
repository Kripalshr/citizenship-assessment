package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @FXML
    public Label remark, marks, markstext, correcttext, wrongtext;

    @FXML
    public ProgressIndicator correct_progress, wrong_progress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int correct = QuizController.getCorrectAnswersCount();
        int wrong = QuizController.getWrongAnswersCount();

        correcttext.setText("Correct Answers : " + correct);
        wrongtext.setText("Incorrect Answers : " + wrong);

        marks.setText(correct + "/" + QuizController.getTotalQuestionsCount());
        float correctPercentage = (float) correct / QuizController.getTotalQuestionsCount();
        correct_progress.setProgress(correctPercentage);

        float wrongPercentage = (float) wrong / QuizController.getTotalQuestionsCount();
        wrong_progress.setProgress(wrongPercentage);

        markstext.setText(correct + " Marks Scored");

        if (correct < 2) {
            remark.setText("Oh no..! You have failed the quiz. It seems that you need to improve your general knowledge. Practice daily! Check your results here.");
        } else if (correct >= 2 && correct < 5) {
            remark.setText("Oops..! You have scored fewer marks. It seems like you need to improve your general knowledge. Check your results here.");
        } else if (correct >= 5 && correct <= 7) {
            remark.setText("Good. A bit more improvement might help you to get better results. Practice is the key to success. Check your results here.");
        } else if (correct == 8 || correct == 9) {
            remark.setText("Congratulations! It's your hard work and determination which helped you to score good marks. Check your results here.");
        } else if (correct == 10) {
            remark.setText("Congratulations! You have passed the quiz with full marks because of your hard work and dedication towards studies. Keep it up! Check your results here.");
        }
    }
    @FXML
    public void handleLogoutButton(ActionEvent event) {
        // Handle the logout button action, e.g., log the user out
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
            Stage loginStage = new Stage();
            Scene loginScene = new Scene(fxmlLoader.load());
            loginStage.setScene(loginScene);

            // Close the current dashboard stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the login stage
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

