package com.example.library_project.main;

import com.example.library_project.alert.pop;
import com.example.library_project.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Arrays;

public class Change {
    @FXML
    private TextField bName;
    @FXML
    private TextField Author;
    @FXML
    private TextField Publish;
    @FXML
    private TextField Number;
    @FXML
    private Button changeButton;
    @FXML
    private Button cancelButton;

    @FXML
    private void cancel(ActionEvent event) {
        ((Stage)cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void ChangeBook(ActionEvent event){
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String name = this.bName.getText();
        String Author = this.Author.getText();
        String Publish = this.Publish.getText();
        String bNumber = this.Number.getText();
        int Number = Integer.parseInt(this.Number.getText());
        if (!name.isEmpty() && !Author.isEmpty() && !Publish.isEmpty() && !bNumber.isEmpty()) {
            Scene scene = bName.getScene();
            int id = (Integer) scene.getUserData();
            String qu = "UPDATE book SET title = '"+name+"', author = '"+Author+"',publisher = '"+Publish+"',number = "+Number+ " WHERE id = " + id;
            if (handler.execAction(qu)) {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Change Successfully", "Success");
                pop.clearInputText(Arrays.asList(this.bName, this.Author, this.Publish, this.Number));
                cancel(event);
            } else {
                pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
            }

        } else {
            pop.showSimpleAlert(Alert.AlertType.ERROR, "Please Enter All Fields", "Error");
        }
    }
}
