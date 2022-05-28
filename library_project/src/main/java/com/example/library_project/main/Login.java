package com.example.library_project.main;

import com.example.library_project.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {
    DatabaseHandler handler = DatabaseHandler.getInstance();

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label loginLabel;
    @FXML
    private Button forgotPassword;
    @FXML
    private Button nMember;

    public Login() {
    }

    @FXML
    private void handleUsernameAction(ActionEvent event) {
        this.password.requestFocus();
    }

    @FXML
    private void handlePasswordAction(ActionEvent event) {
        this.loginBtn.fireEvent(new ActionEvent());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handler = DatabaseHandler.getInstance();
    }
    @FXML
    private void login(ActionEvent event) throws SQLException {
        int id = 0;
        String uname = this.username.getText();
        String pass = this.password.getText();
        String qu = "SELECT id from member where username = '"+uname +"' and password = '"+pass+"'";
        ResultSet result = handler.execQuery(qu);
        while(result.next()) {
             id = result.getInt("id");
        }
        if (id==1) {
            ((Stage)this.username.getScene().getWindow()).close();
            this.loadWindow("staff.fxml", "Library Management System");
            handleCancel(event);
        } else if(id>1){
            qu = "update middle set id = "+id+" limit 1 ";
            handler.execUpdate(qu);
            ((Stage)this.username.getScene().getWindow()).close();
            this.loadWindow("Member.fxml", "Library System");
            handleCancel(event);
        } else{
            this.loginLabel.setText("Invalid Credentials");
            this.loginLabel.setStyle("-fx-background-color: red;-fx-text-fill: white;");
        }

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
    @FXML
    private void ForgetPassword (ActionEvent event) throws SQLException{
        this.loadWindow("forgetPassword.fxml", "Forget Password");
    }

    @FXML
    private void nMember(ActionEvent event){
        this.loadWindow("newMember.fxml", "New Member");
    }


}
