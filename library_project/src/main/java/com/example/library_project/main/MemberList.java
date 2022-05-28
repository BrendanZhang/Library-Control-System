package com.example.library_project.main;

import com.example.library_project.database.DatabaseHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class MemberList implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loaddata();
    }

    ObservableList<Member> list = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> mobileCol;
    @FXML
    private TableColumn<Member, String> emailCol;


    private void loaddata() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM member";
        ResultSet result = handler.execQuery(qu);
        try {
            while (result.next()) {
                String name = result.getString("username");
                int id = Integer.parseInt(result.getString("id"));
                String mobile = result.getString("mobile");
                String email = result.getString("email");
                list.add(new Member(name, id, mobile, email));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        tableView.getItems().setAll(list);
    }

    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    public static class Member {
        private final String name;
        private final int id;
        private final String mobile;
        private final String email;

        Member(String name, int id, String mobile, String email) {
            this.name =name;
            this.id = id;
            this.mobile = mobile;
            this.email = email;
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }

        public String getMobile() {
            return this.mobile;
        }

        public String getEmail() {
            return this.email;
        }

    }
}
