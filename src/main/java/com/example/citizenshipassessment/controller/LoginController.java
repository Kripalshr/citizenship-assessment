package com.example.citizenshipassessment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginController {
    @FXML
    private Label loginVerify;

   @FXML
   protected void onLoginButtonClick(){
       loginVerify.setText("Successefully Loggedin");
   }
}
