package com.example.library_project.main;

import com.example.library_project.alert.pop;
import com.example.library_project.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;



public class forgetPassword implements Initializable {
    DatabaseHandler handler;
    public static int id;

    @FXML
    private TextField username;
    @FXML
    private TextField mobile;
    @FXML
    private Button checkBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label loginLabel;
    @FXML
    private TextField NewPassword;
    @FXML
    private Button confirmBtn;
    @FXML
    private Button cancelBtn1;
    @FXML
    private Label loginLabel1;

    public forgetPassword(){

    }

    @FXML
    private void handleUsernameAction(ActionEvent event) {
        this.mobile.requestFocus();
    }

    @FXML
    private void handleMobileAction(ActionEvent event) {
        this.checkBtn.fireEvent(new ActionEvent());
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage)this.username.getScene().getWindow()).close();
    }

    private void loadWindow(String loc, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
    }

    public void check (ActionEvent event) throws SQLException {
        boolean a= false;
        String uname = this.username.getText();
        String mobile = this.mobile.getText();
        String qu = "SELECT id from member where username = '"+uname +"' and mobile = '"+mobile+"'";
        ResultSet result = handler.execQuery(qu);
        while(result.next()){
            loadWindow("setPassword.fxml", "set new Password");
            id=(result.getInt("id"));

            a=true;
            handleCancel(event);
        } if(!a){
            pop.showSimpleAlert(Alert.AlertType.ERROR, "Username or mobile have mistakes", "Failed");
        }
    }

    @FXML
    private void handlePasswordAction(ActionEvent event) {
        this.confirmBtn.requestFocus();
    }

    @FXML
    private void handleCancel1(ActionEvent event) {
        ((Stage)this.NewPassword.getScene().getWindow()).close();
    }

    public void Confirm (ActionEvent event) throws SQLException {
        forgetPassword a= new forgetPassword();
        String password = this.NewPassword.getText();
        String qu = "update member set password = '"+password+"' where id = "+id;
        if (handler.execUpdate(qu)) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Change Password Successfully", "Success");
            pop.clearInputText(List.of(NewPassword));
            handleCancel1(event);
        } else {
            pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
        }
    }
}
