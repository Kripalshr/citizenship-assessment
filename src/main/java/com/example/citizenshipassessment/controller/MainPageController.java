package com.example.citizenshipassessment.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainPageController {
    @FXML
    private Label welcomeLabel;

    public void initialize() {
        // Initialization code can go here (e.g., setting initial UI state).
        welcomeLabel.setText("Welcome to the Main Page!");
    }

    @FXML
    public void handleLogoutButton(ActionEvent event) {
        // Add code to handle the "Logout" button's action, which should navigate back to the login page.
        // You can use a similar approach as in your LoginController to switch scenes.
    }

    // You can define additional event handlers and methods as needed for your main page.
}