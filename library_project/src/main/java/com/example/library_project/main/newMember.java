package com.example.library_project.main;

import com.example.library_project.alert.pop;
import com.example.library_project.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class newMember implements Initializable {
    DatabaseHandler handler;
    @FXML
    private TextField mName;
    @FXML
    private TextField password;
    @FXML
    private TextField mMobile;
    @FXML
    private TextField mEmail;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    public newMember() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
    }

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void addMember(ActionEvent event){
        String name = this.mName.getText();
        String password = this.password.getText();
        String mobile = this.mMobile.getText();
        String email = this.mEmail.getText();
        people a = new people(name,mobile,email,password);
        if (!name.isEmpty() && !password.isEmpty() && !mobile.isEmpty() && !email.isEmpty()) {
            String qu = "INSERT INTO member values(" + a.getId() + ",'" + name + "','" + mobile + "','" + email + "','"+password + "',"+a.getnBorrowed()+ ","+a.getFinePerDay() +")";
            if (handler.execAction(qu)) {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Member Added Successfully", "Success");
                pop.clearInputText(Arrays.asList(this.mName, this.password, this.mMobile, this.mEmail));
            } else {
                pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
            }

        } else {
            pop.showSimpleAlert(Alert.AlertType.ERROR, "Please Enter All Fields", "Error");
        }
    }
}
