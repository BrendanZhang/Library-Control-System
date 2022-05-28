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
import java.util.ResourceBundle;

public class Setting implements Initializable {
    DatabaseHandler handler;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField finePerDay;
    @FXML
    private TextField nDaysWithoutFine;
    @FXML
    private TextField nBorrow;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handler = DatabaseHandler.getInstance();
        initiDefaultValues();

    }

    @FXML
    private void handleNDaysWithoutFineAction(ActionEvent event) {
        finePerDay.requestFocus();
    }

    @FXML
    private void handleFinePerDayAction(ActionEvent event) {
        nBorrow.requestFocus();
    }
    @FXML
    private void handleBorrowedAction(ActionEvent event) {
        saveButton.fireEvent(new ActionEvent());
    }


    @FXML
    private void saveButtonHandler(ActionEvent event) {
        try {
            int nDays = Integer.parseInt(nDaysWithoutFine.getText());
            float fine = Float.parseFloat(finePerDay.getText());
            int nBorrowed=Integer.parseInt(nBorrow.getText());
            String qu = "update middle set nBorrowed = "+nBorrowed+", FinePerDay = "+fine+",BorrowedDate = "+nDays+" limit 1";
            if (handler.execUpdate(qu)) {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Settings Saved Successfully", "Success");
            }
        } catch (NumberFormatException e) {
            pop.showSimpleAlert(Alert.AlertType.ERROR, "Please Enter Valid Number", "Invalid Number");
        }
    }

    @FXML
    private void cancelButtonHandler(ActionEvent event) {
        ((Stage) finePerDay.getScene().getWindow()).close();
    }

    private void initiDefaultValues() {
        String qu = "Select * from middle limit 1";
        ResultSet result = handler.execQuery(qu);
        while(true) {
            try {
                if (!result.next()) break;
                finePerDay.setText(String.valueOf(result.getInt("FinePerDay")));
                nDaysWithoutFine.setText(String.valueOf(result.getFloat("BorrowedDate")));
                nBorrow.setText(String.valueOf(result.getFloat("nBorrowed")));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }
}
