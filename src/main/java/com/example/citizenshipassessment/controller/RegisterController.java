package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.MainApp;
import com.example.citizenshipassessment.database.DatabaseConnector;
import com.example.citizenshipassessment.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;

public class RegisterController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField first_name;
    @FXML
    private TextField last_name;
    @FXML
    private ToggleGroup GenderGroup;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton otherRadioButton;
    @FXML
    private TextField citizenshipTextField;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private ChoiceBox<String> nationalityPicker;

    private DatabaseConnector dbConnector;
    private Stage stage;
    private Scene scene;

    @FXML
    private void initialize() {
        // Create a list of nationality choices
        ObservableList<String> nationalityChoices = FXCollections.observableArrayList("United States", "United Kingdom", "Canada", "Australia", "India", "Other");

        // Add choices to the ChoiceBox
        nationalityPicker.setItems(nationalityChoices);
    }

    public RegisterController() {
        dbConnector = new DatabaseConnector(AppConfig.DB_URL, AppConfig.DB_USERNAME, AppConfig.DB_PASSWORD);
    }

    public void onRegisterButtonClick(ActionEvent event) throws IOException {
        // Check if the username already exists
        if (isUsernameTaken(usernameField.getText())) {
            // Handle the case where the username is already taken
            showAlert("Error", "Username already exists. Please choose a different username.");
            return; // Stop the registration process
        }
        if (isEmailTaken(emailField.getText())) {
            showAlert("Error", "Email is already taken. Please choose a different email.");
            return;
        }

        // Continue with user registration
        User user = new User();
        user.setUsername(usernameField.getText());
        user.setPassword(passwordField.getText());
        user.setEmail(emailField.getText());
        user.setFirstName(first_name.getText());
        user.setLastName(last_name.getText());
        user.setDob(Date.valueOf(dobDatePicker.getValue()));
        user.setGender(getSelectedGender());
        user.setCountry(nationalityPicker.getValue());
        user.setCitizenshipNumber(citizenshipTextField.getText());

        // Insert user into the database
        dbConnector.insertUser(user);

        // Switch to the login page
        switchToLoginPage(event);
    }

    private boolean isUsernameTaken(String username) {
        try {
            User existingUser = dbConnector.getUserByUsername(username);
            return existingUser != null;
        } catch (RuntimeException e) {
            e.printStackTrace(); // Handle exceptions appropriately in your application
            return true; // Assume an error occurred, and prevent registration
        }
    }
    private boolean isEmailTaken(String email) {
        try {
            User existingUser = dbConnector.getUserByEmail(email);
            return existingUser != null;
        } catch (RuntimeException e) {
            e.printStackTrace(); // Handle exceptions appropriately in your application
            return true; // Assume an error occurred, and prevent registration
        }
    }

    private String getSelectedGender() {
        RadioButton selectedRadioButton = (RadioButton) GenderGroup.getSelectedToggle();
        return selectedRadioButton != null ? selectedRadioButton.getText() : null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void switchToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
