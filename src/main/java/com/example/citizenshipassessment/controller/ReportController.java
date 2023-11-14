package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    @FXML
    private ChoiceBox<String> candidates;

    private String loggedInUsername;

    @FXML
    private Label firstname;
    @FXML
    private Label lastname;
    @FXML
    private Label username;
    @FXML
    private Label score;
    @FXML
    private AnchorPane results;
    @FXML
    private Label resultMessage;
    @FXML
    private Rectangle passFailIndicator;
    @FXML
    private Label passFailText;

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
        userName.setText(loggedInUsername);
    }

    // You can add methods to set values for these labels if needed
    public void initialize(){
        calculateAndSetStatistics();
        populateCandidateChoiceBox();
    }

    private void populateCandidateChoiceBox() {
        // Fetch names from the database and add them to the ChoiceBox
        List<String> firstNames = fetchFirstNamesFromDatabase();
        candidates.getItems().addAll(firstNames);

        candidates.setOnAction(event -> {
            String selectedFirstName = candidates.getValue();
            String selectedUsername = fetchUsernameByFirstName(selectedFirstName);
            String[] userData = fetchUserDetailsAndScore(selectedUsername);
            firstname.setText(userData[0]);
            lastname.setText(userData[1]);
            username.setText(userData[2]);
            score.setText(userData[3]);
            // You can use selectedUsername as needed

            results.setVisible(true);
            resultMessage.setVisible(true);
//            int userScore = Integer.parseInt(userData[3]);
//            System.out.println(userScore);
            if (userData[3] != null && !userData[3].isEmpty()) {
                int userScore = Integer.parseInt(userData[3]);

                if (userScore < 10) {
                    // Display sorry message
                    resultMessage.setText("Sorry");
                    passFailIndicator.setFill(Color.RED);
                    passFailText.setText("Fail");
                    passFailText.setTextFill(Color.WHITE);
                } else {
                    // Display congratulation message
                    resultMessage.setText("Congratulations");
                    passFailIndicator.setFill(Color.GREEN);
                    passFailText.setText("Pass");
                    passFailText.setTextFill(Color.WHITE);
                }
            } else {
                // Handle the case where the user has no score (null or empty)
                resultMessage.setText("No score available");
                passFailIndicator.setFill(Color.GRAY);
                passFailText.setText("NO Score Available");
                passFailText.setTextFill(Color.WHITE);
            }
        });
    }

    private String[] fetchUserDetailsAndScore(String username) {
        String url = AppConfig.DB_URL;
        String dbUsername = AppConfig.DB_USERNAME;
        String dbPassword = AppConfig.DB_PASSWORD;

        String[] userDetailsAndScore = new String[5]; // Assuming you want to store firstname, lastname, username, score

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            // Fetch user details from the users table
            String userDetailsQuery = "SELECT f_name, l_name, username FROM users WHERE username = ?";
            try (PreparedStatement userDetailsStatement = connection.prepareStatement(userDetailsQuery)) {
                userDetailsStatement.setString(1, username);
                try (ResultSet userDetailsResultSet = userDetailsStatement.executeQuery()) {
                    if (userDetailsResultSet.next()) {
                        userDetailsAndScore[0] = userDetailsResultSet.getString("f_name"); // firstname
                        userDetailsAndScore[1] = userDetailsResultSet.getString("l_name"); // lastname
                        userDetailsAndScore[2] = userDetailsResultSet.getString("username"); // username
                    }
                }
            }

            // Fetch score from the answers table
            String scoreQuery = "SELECT marks_obtained FROM answers WHERE username = ?";
            try (PreparedStatement scoreStatement = connection.prepareStatement(scoreQuery)) {
                scoreStatement.setString(1, username);
                try (ResultSet scoreResultSet = scoreStatement.executeQuery()) {
                    if (scoreResultSet.next()) {
                        userDetailsAndScore[3] = scoreResultSet.getString("marks_obtained"); // score
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in your application
        }

        return userDetailsAndScore;
    }


    private String fetchUsernameByFirstName(String firstName) {
        String url = AppConfig.DB_URL;
        String dbUsername = AppConfig.DB_USERNAME;
        String dbPassword = AppConfig.DB_PASSWORD;
        String selectedUsername = null;

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT username FROM users WHERE f_name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, firstName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        selectedUsername = resultSet.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in your application
        }

        return selectedUsername;
    }



    private List<String> fetchFirstNamesFromDatabase() {
        String url = AppConfig.DB_URL;
        String dbUsername = AppConfig.DB_USERNAME;
        String dbPassword = AppConfig.DB_PASSWORD;
        List<String> userNames = new ArrayList<>();
        List<String> firstNames = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT username,f_name FROM users"; // Replace with your actual table name
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    userNames.add(resultSet.getString("username"));
                    firstNames.add(resultSet.getString("f_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately in your application
        }
        return firstNames;
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
