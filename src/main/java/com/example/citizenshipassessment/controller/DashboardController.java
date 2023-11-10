package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label userName;
    private Stage stage;
    private Scene scene;

    private String loggedInUsername;
    @FXML
    private Button logout;



    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
        userName.setText(loggedInUsername);
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
