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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DashboardController {

    @FXML
    private Label userName;

    @FXML
    private ImageView flagImage;
    private Stage stage;
    private Scene scene;

    private String loggedInUsername;
    @FXML
    private Button logout;
    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
        System.out.println(loggedInUsername);
        userName.setText(loggedInUsername);
    }

    public void setFlag() {
        String country = fetchUserNationality(loggedInUsername);
        System.out.println(country);

        if (country != null) {
            // Assuming you have image resources named "flag_singapore.png", "flag_usa.png", etc.
            String imagePath = "/com/example/citizenshipassessment/images/" + country.toLowerCase() + ".png";

            // Load the image dynamically using resource path
            Image image = new Image(getClass().getResourceAsStream(imagePath));

            // Set the image to your ImageView (replace 'flagImage' with the actual name of your ImageView)
            flagImage.setImage(image);
        }
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

    public void handleStartAssessmentButton(ActionEvent event) {
        // Fetch user information from the database
        Date dob = fetchUserDOB(loggedInUsername);

        // Check if the user is above 18
        if (isUserAbove18(dob)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("quiz.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(fxmlLoader.load());

                // Get the controller from the FXMLLoader
                QuizController quizController = fxmlLoader.getController();

                // Pass the logged-in username to QuizController
                quizController.setLoggedInUsername(loggedInUsername);

                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert indicating the user needs to be 18 or older
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Age Verification");
            alert.setHeaderText(null);
            alert.setContentText("You must be 18 years or older to take this assessment.");
            alert.showAndWait();
        }
    }

    private Date fetchUserDOB(String username) {
        // Modify these variables with your database connection details
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

    private String fetchUserNationality(String username) {
        // Modify these variables with your database connection details
        String url = AppConfig.DB_URL;
        String dbUsername = AppConfig.DB_USERNAME;
        String dbPassword = AppConfig.DB_PASSWORD;

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String sql = "SELECT country FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();


                if (resultSet.next()) {
                    return resultSet.getString("country");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    @FXML
    public void handleCheckReport(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("report.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());

            // Get the controller from the FXMLLoader
            ReportController reportController = fxmlLoader.getController();

            // Pass the logged-in username to ReportController
            reportController.setLoggedInUsername(loggedInUsername);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
