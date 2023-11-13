package com.example.citizenshipassessment.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultController implements Initializable {

    @FXML
    public Label remark, marks, markstext, correcttext, wrongtext, percentageLabel;

    @FXML
    public ProgressIndicator correct_progress, wrong_progress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int correct = QuizController.getCorrectAnswersCount();
        int wrong = QuizController.getWrongAnswersCount();
        int totalQuestions = QuizController.getTotalQuestionsCount();

        correcttext.setText("Correct Answers: " + correct);
        wrongtext.setText("Incorrect Answers: " + wrong);

        marks.setText(correct + "/" + totalQuestions);

        float correctPercentage = ((float) correct / totalQuestions) * 100;
        correct_progress.setProgress(correctPercentage / 100);

        float wrongPercentage = ((float) wrong / totalQuestions) * 100;
        wrong_progress.setProgress(wrongPercentage / 100);

        markstext.setText(correct + " Marks Scored");

        // Calculate and display the percentage dynamically
        float totalPercentage = ((float) correct / totalQuestions) * 100;
        percentageLabel.setText("Percentage Scored: " + String.format("%.2f", totalPercentage) + "%");

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
}
