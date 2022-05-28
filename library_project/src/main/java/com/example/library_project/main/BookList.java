package com.example.library_project.main;

import com.example.library_project.alert.pop;
import com.example.library_project.database.DatabaseHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookList implements Initializable {
    ObservableList<Book> list = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private TableColumn<Book, Integer> numberCol;

    @FXML
    private TextField BId;

    @FXML
    private Button DeleteBt;

    @FXML
    private Button ChangeBt;



    public BookList() {

    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initCol();
        loaddata();
    }



    private void loaddata() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM book";
        ResultSet result = handler.execQuery(qu);
        try {
            while (result.next()) {
                String title = result.getString("title");
                int id = result.getInt("id");
                String author = result.getString("author");
                String publisher = result.getString("publisher");
                int number = result.getInt("number");
                list.add(new Book(title, id, author, publisher, number));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        tableView.getItems().setAll(list);
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
    }

    public static class Book {
        private final String title;
        private final int id;
        private final String author;
        private final String publisher;
        private final int number;

        Book(String title, int id, String author, String publisher, int number) {
            this.title = title;
            this.id = id;
            this.author = author;
            this.publisher = publisher;
            this.number = number;
        }

        public String getTitle() {
            return title;
        }

        public int getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getPublisher() {
            return publisher;
        }

        public int getNumber() {
            return number;
        }
    }

    @FXML
    private void Delete(ActionEvent event) {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String query = "DELETE FROM book WHERE id = " + Integer.parseInt(BId.getText()) ;
        Optional<ButtonType> response = pop.showConfirmAlert("Are you sure want to Delete this book ?", "Book Delete");
        if (response.get() == ButtonType.OK) {
            if (handler.execAction(query) ) {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Delete Successfully", "success");
            }else {
                pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
            }
        } else {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Cancelled", "Cancelled");
        }

    }

    @FXML
    private void change(ActionEvent event) {
        this.loadWindow("Change.fxml", "Change");

    }

    private void loadWindow(String loc, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(loc));
            Scene scene = new Scene(root);
            scene.setUserData(Integer.parseInt(BId.getText()));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
