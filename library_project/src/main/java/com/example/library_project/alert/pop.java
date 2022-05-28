package com.example.library_project.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class pop {
    private static Alert alert;

    public pop() {
    }

    public static void showSimpleAlert(Alert.AlertType type, String message, String title) {
        alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<ButtonType> showConfirmAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(message);
        Optional<ButtonType> response = alert.showAndWait();
        return response;
    }

    public static void clearInputText(List<TextField> inputList) {
        Iterator var1 = inputList.iterator();

        while(var1.hasNext()) {
            TextField input = (TextField)var1.next();
            input.setText("");
        }

    }

    public static void clearText(List<Text> texts) {
        Iterator var1 = texts.iterator();

        while(var1.hasNext()) {
            Text text = (Text)var1.next();
            text.setText("");
        }

    }
}
