package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

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

    private int calculateAge(Date birthDate) {
        if (birthDate != null) {
            long currentTime = System.currentTimeMillis();
            long birthTime = birthDate.getTime();
            long millisecondsPerYear = 1000L * 60 * 60 * 24 * 365;
            return (int) ((currentTime - birthTime) / millisecondsPerYear);
        }
        return 0;
    }

    private boolean isUserAbove18(Date birthDate) {
        int age = calculateAge(birthDate);
        return age >= 18;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    @FXML
    public void handleStartAssessmentButton(ActionEvent event) {
        Date dob = fetchUserDOB(loggedInUsername);

        if (dob != null && isUserAbove18(dob)) {
            try {
                loadFXML("quiz.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Age Verification", "You must be 18 years or older to take this assessment.");
        }
    }

    @FXML
    public void handleResultsButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("report.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFXML(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        stage = (Stage) userName.getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Date fetchUserDOB(String username) {
        String url = AppConfig.DB_URL;
        String dbUsername = AppConfig.DB_USERNAME;
        String dbPassword = AppConfig.DB_PASSWORD;

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String sql = "SELECT DOB FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getDate("DOB");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @FXML
    public void handleProfileButton(ActionEvent event) {
        // Handle the profile button action, e.g., open the user's profile
    }

    @FXML
    public void handleLogoutButton(ActionEvent event) {
        try {
            loadFXML("login-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
