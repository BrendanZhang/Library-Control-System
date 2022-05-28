package com.example.library_project.main;

import com.example.library_project.alert.pop;
import com.example.library_project.database.DatabaseHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

public class test {
    private static Connection conn;
    private static final String DB_URL = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) throws SQLException {
        boolean a= false;
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT id from member where username = "+"'a'" +"and mobile = "+"'12'";
        ResultSet result = handler.execQuery(qu);
        while(result.next()){
            test b= new test();
            b.test1();
            a=true;
        } if(!a){
            pop.showSimpleAlert(Alert.AlertType.ERROR, "Username or mobile have mistakes", "Failed");
        }





//        DatabaseHandler handler = DatabaseHandler.getInstance();
//        String qu = "select * from member order by id DESC limit 1;";
//        ResultSet result = handler.execQuery(qu);
//        try {
//            while (result.next()) {
//                String name = result.getString("username");
//                int id = result.getInt("id");
//                String mobile = result.getString("mobile");
//                String email = result.getString("email");
//                System.out.println(name+id+mobile+email);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }

//        people a =new people("a","123","qwe","qwert");
//        System.out.println(a.getId());
//        test a =new test();
//        a.test1();
//        ResultSet result = handler.execQuery(qu);
//        int id = result.getInt("id");
//        System.out.println(id);
    }
    private void test1(){
        loadWindow("newMember.fxml", "Forget Password");
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
}

