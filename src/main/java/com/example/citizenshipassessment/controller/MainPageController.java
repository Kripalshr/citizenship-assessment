package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.MainApp;
import com.example.citizenshipassessment.database.DatabaseConnector;
import com.example.citizenshipassessment.model.CitizenshipData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.Date;

public class MainPageController {

    @FXML
    private Label username;

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

    private String loggedInUsername;
    private Stage stage;
    private Scene scene;

    @FXML
    public void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    public void initialize() {
        username.setText(loggedInUsername);
    }

    public void handleFormSubmit(ActionEvent event) {
        RadioButton selectedGender = (RadioButton) gender.getSelectedToggle();
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
        DatabaseConnector dbConnector = new DatabaseConnector(AppConfig.DB_URL, AppConfig.DB_USERNAME, AppConfig.DB_PASSWORD);
        dbConnector.insertCitizenshipData(citizenshipData);
        dbConnector.close();
        loadDashboardPage(event);
    }

    public void loadDashboardPage(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("dashboard-page.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogoutButton(ActionEvent event) {
        // Handle logout
    }
}
