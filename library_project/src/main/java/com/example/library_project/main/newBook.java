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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class newBook implements Initializable {
    DatabaseHandler handler;
    @FXML
    private TextField bName;
    @FXML
    private TextField Author;
    @FXML
    private TextField Publish;
    @FXML
    private TextField Number;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    public newBook() {
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
    private void addBook(ActionEvent event){
        String name = this.bName.getText();
        String Author = this.Author.getText();
        String Publish = this.Publish.getText();
        String bNumber = this.Number.getText();
        int Number = Integer.parseInt(this.Number.getText());
        book a = new book(name,Author,Publish,Number);
        if (!name.isEmpty() && !Author.isEmpty() && !Publish.isEmpty() && !bNumber.isEmpty()) {
            String qu = "SELECT * FROM book WHERE title = '" + name +"' and author = '"+ Author +"' and publisher = '"+ Publish +"'" ;
            ResultSet rs = this.handler.execQuery(qu);
            try {
                if (!rs.next()) {
                    qu = "INSERT INTO book values(" + a.getId() + ",'" + name + "','" + Author+ "','" + Publish + "',"+ Number + ")";
                    if (handler.execAction(qu)) {
                        pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Added Successfully", "Success");
                        pop.clearInputText(Arrays.asList(this.bName, this.Author, this.Publish, this.Number));
                }else {
                        pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
                    }
            }else {
                    pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Already have this book", "Failed");
                    pop.clearInputText(Arrays.asList(this.bName, this.Author, this.Publish, this.Number));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            pop.showSimpleAlert(Alert.AlertType.ERROR, "Please Enter All Fields", "Error");
        }
    }
}
