package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.database.DatabaseConnector;
import com.example.citizenshipassessment.model.CitizenshipData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.ToggleGroup;
import org.w3c.dom.Text;

import java.sql.Date;

public class MainPageController {
    @FXML
    private HBox mainContainer;

    @FXML
    private Label username;

    @FXML
    private AnchorPane innerContainer;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField middleNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField nationalityTextField;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TextField fathersFirstName;

    @FXML
    private TextField fathersMiddleName;

    @FXML
    private TextField mothersFirstName;

    @FXML
    private TextField mothersMiddleName;

    @FXML
    private TextField mothersLastName;

    @FXML
    private TextField fathersLastName;
    
    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton otherRadioButton;

    @FXML
    private TextField citizenshipTextField;

    @FXML
    private TextField issuingCountryTextField;

    @FXML
    private Button submitButton;

    @FXML
    private ToggleGroup gender;

    private String loggedInUsername; // Added field for storing the logged-in username

    // Define a ToggleGroup for the radio buttons

    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
        System.out.println(username);
    }

    public void initialize() {
        username.setText(loggedInUsername);
        // Initialization code can go here (e.g., setting initial UI state).
        System.out.println(loggedInUsername);
    }
    // In your MainPageController
    public void handleFormSubmit(ActionEvent event) {

        RadioButton selectedGender = (RadioButton) gender.getSelectedToggle();
        // Collect data from the form fields and create a CitizenshipData instance
        CitizenshipData citizenshipData = new CitizenshipData();
        citizenshipData.setFirstName(firstNameTextField.getText());
        citizenshipData.setMiddleName(middleNameTextField.getText());
        citizenshipData.setLastName(lastNameTextField.getText());
        citizenshipData.setNationality(nationalityTextField.getText());
        citizenshipData.setDob(Date.valueOf(dobDatePicker.getValue()));
        citizenshipData.setFatherFirstName(fathersFirstName.getText());
        citizenshipData.setFatherMiddleName(fathersMiddleName.getText());
        citizenshipData.setMotherFirstName(mothersFirstName.getText());
        citizenshipData.setMotherMiddleName(mothersMiddleName.getText());
        citizenshipData.setMotherLastName(mothersLastName.getText());
        citizenshipData.setFatherLastName(fathersLastName.getText());
        citizenshipData.setGender(selectedGender.getText());
        citizenshipData.setCitizenshipNumber(citizenshipTextField.getText());
        citizenshipData.setIssuingCountry(issuingCountryTextField.getText());
        // Insert the data into the database
        DatabaseConnector dbConnector = new DatabaseConnector(AppConfig.DB_URL, AppConfig.DB_USERNAME, AppConfig.DB_PASSWORD);
        dbConnector.insertCitizenshipData(citizenshipData);
        dbConnector.close();
    }

    @FXML
    public void handleLogoutButton(ActionEvent event) {

    }

    // You can define additional event handlers and methods as needed for your main page.
}
