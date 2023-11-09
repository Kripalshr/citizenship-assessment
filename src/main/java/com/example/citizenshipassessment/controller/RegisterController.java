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
        RadioButton selectedRadioButton = (RadioButton) GenderGroup.getSelectedToggle();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String f_name = first_name.getText();
        String l_name = last_name.getText();
        String nationality = nationalityPicker.getValue();
        String citizenshipNumber = citizenshipTextField.getText();

        // You can add additional validation here

        // Hash the password before storing it in the database

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(f_name);
        user.setLastName(l_name);
        user.setDob(Date.valueOf(dobDatePicker.getValue()));
        if (selectedRadioButton != null) {
            user.setGender(selectedRadioButton.getText());
        }
        user.setNationality(nationality);
        user.setCitizenshipNumber(citizenshipNumber);
        dbConnector.insertUser(user);
        switchToLoginPage(event);
    }

    public void switchToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
