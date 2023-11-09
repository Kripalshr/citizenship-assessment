package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label welcomeLabel;
    private Stage stage;
    private Scene scene;

    private String loggedInUsername;

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }
    public void handleStartAssessmentButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("quiz.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleProfileButton(ActionEvent event) {
        // Handle the profile button action, e.g., open the user's profile
    }

    @FXML
    public void handleLogoutButton(ActionEvent event) {
        // Handle the logout button action, e.g., log the user out
    }
}
