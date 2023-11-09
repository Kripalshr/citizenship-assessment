package com.example.citizenshipassessment.controller;

import com.example.citizenshipassessment.AppConfig;
import com.example.citizenshipassessment.MainApp;
import com.example.citizenshipassessment.database.DatabaseConnector;
import com.example.citizenshipassessment.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private Label loginVerify;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button login_button;
    private DatabaseConnector dbConnector;

    private Stage stage;
    private Scene scene;

    public LoginController() {
        dbConnector = new DatabaseConnector(AppConfig.DB_URL, AppConfig.DB_USERNAME, AppConfig.DB_PASSWORD);
    }

    public void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = dbConnector.getUserByUsername(username);

        if (user != null && (Objects.equals(password, user.getPassword()))) {
            loadMainPage(event, username);
        } else {
            loginVerify.setText("Login Failed");
        }
    }

    private void loadMainPage(ActionEvent event, String username){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("dashboard-page.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load());
            // Access the MainPageController instance from the FXMLLoader
            DashboardController dashboardController = fxmlLoader.getController();

            // Set the logged-in username in MainPageController
            dashboardController.setLoggedInUsername(username);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToRegisterPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("register-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

}
