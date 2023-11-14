package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportController {
    @FXML
    private Label userName;

    @FXML
    private Label calculateMean;

    @FXML
    private Label calculateMedian;

    @FXML
    private Label calculateMode;

    @FXML
    private Label calculateSD;

    @FXML
    private Label calculateMin;

    @FXML
    private Label calculateMax;

    private String loggedInUsername;

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
        userName.setText(loggedInUsername);
    }

    // You can add methods to set values for these labels if needed
    public void initialize(){
        calculateAndSetStatistics();
    }


    public void setCalculateMean(String meanValue) {
        this.calculateMean.setText(meanValue);
    }

    public void setCalculateMedian(String medianValue) {
        this.calculateMedian.setText(medianValue);
    }

    public void setCalculateMode(String modeValue) {
        this.calculateMode.setText(modeValue);
    }

    public void setCalculateSD(String sdValue) {
        this.calculateSD.setText(sdValue);
    }

    public void setCalculateMin(String minValue) {
        this.calculateMin.setText(minValue);
    }

    public void setCalculateMax(String maxValue) {
        this.calculateMax.setText(maxValue);
    }
    @FXML
    public void onLogout(ActionEvent event) {
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
    private void calculateAndSetStatistics() {
        // Fetch marks_obtained from the database
        List<Integer> marksList = fetchMarksFromDatabase();

        // Calculate and set mean, median, mode, standard deviation, min marks, and max marks
        if (marksList != null && !marksList.isEmpty()) {
            setCalculateMean(String.valueOf(calculateMean(marksList)));
            setCalculateMedian(String.valueOf(calculateMedian(marksList)));
            setCalculateMode(String.valueOf(calculateMode(marksList)));
            setCalculateSD(String.valueOf(calculateSD(marksList)));
            setCalculateMin(String.valueOf(Collections.min(marksList)));
            setCalculateMax(String.valueOf(Collections.max(marksList)));
        }
    }

    private List<Integer> fetchMarksFromDatabase() {
        String url = AppConfig.DB_URL;
        String dbUsername = AppConfig.DB_USERNAME;
        String dbPassword = AppConfig.DB_PASSWORD;
        List<Integer> marksList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword) ) {
            String query = "SELECT marks_obtained FROM answers"; // Replace with your actual table name
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    marksList.add(resultSet.getInt("marks_obtained"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in your application
        }
        return marksList;
    }

    private double calculateMean(List<Integer> marksList) {
        // Implement your logic to calculate the mean
        // For demonstration, let's assume a simple calculation
        return marksList.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
    }

    private double calculateMedian(List<Integer> marksList) {
        // Implement your logic to calculate the median
        // For demonstration, let's assume a simple calculation
        Collections.sort(marksList);
        int size = marksList.size();
        return size % 2 == 0 ?
                (marksList.get(size / 2 - 1) + marksList.get(size / 2)) / 2.0 :
                marksList.get(size / 2);
    }

    private int calculateMode(List<Integer> marksList) {
        // Implement your logic to calculate the mode
        // For demonstration, let's assume a simple calculation
        return marksList.stream()
                .max((a, b) -> Collections.frequency(marksList, a) -
                        Collections.frequency(marksList, b))
                .orElse(0);
    }

    private double calculateSD(List<Integer> marksList) {
        // Implement your logic to calculate the standard deviation
        // For demonstration, let's assume a simple calculation
        double mean = calculateMean(marksList);
        double sum = marksList.stream().mapToDouble(m -> Math.pow(m - mean, 2)).sum();
        return Math.sqrt(sum / marksList.size());
    }
}
