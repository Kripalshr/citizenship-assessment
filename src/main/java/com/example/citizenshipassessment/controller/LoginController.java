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
            loginVerify.setText("Successfully Logged in");
        } else {
            loginVerify.setText("Login Failed");
        }
    }

    public void switchToRegisterPage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("register-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }


    @FXML
   protected void onLoginButtonClick(){
       loginVerify.setText("Successefully Loggedin");
   }
}
