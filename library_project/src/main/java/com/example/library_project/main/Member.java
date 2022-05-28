package com.example.library_project.main;

import com.example.library_project.alert.pop;
import com.example.library_project.database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class Member implements Initializable {
    DatabaseHandler handler;
    int id;
    ObservableList<BookList.Book> list = FXCollections.observableArrayList();
    Boolean isReadyForSubmission = false;
    Boolean isNeedToPay= false;


    @FXML
    private TextField SBName;
    @FXML
    private TextField BBid;
    @FXML
    private TextField bIdR;
    @FXML
    private TableColumn<BookList.Book, String> titleCol;

    @FXML
    private TableColumn<BookList.Book, String> idCol;

    @FXML
    private TableColumn<BookList.Book, String> authorCol;

    @FXML
    private TableColumn<BookList.Book, String> publisherCol;

    @FXML
    private TableColumn<BookList.Book, Boolean> numberCol;

    @FXML
    private TableView<BookList.Book> BTableView;
    @FXML
    private ListView<String> RTableView;
    @FXML
    private Button SearchBtn;
    @FXML
    private Button BorrowBtn;
    @FXML
    private Button ReturnBtn;
    @FXML
    private Text mName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.handler = DatabaseHandler.getInstance();
        String qu = "select id from middle  limit 1 ";
        ResultSet result=handler.execQuery(qu);
        while (true){
            try {
                if (!result.next()) break;
                this.id=result.getInt("id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        qu="select username from member  where id="+id;
        result=handler.execQuery(qu);
        while (true){
            try {
                if (!result.next()) break;
               mName.setText(result.getString("username"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        loaddata();
        initCol();
        ReturnBook ();
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
                list.add(new BookList.Book(title, id, author, publisher, number));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        BTableView.getItems().setAll(list);
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("number"));
    }
    public static class BBook {
        private final String name;
        private final int id;
        private final Date Time;


        BBook(String title, int id, Date Time) {
            this.name = title;
            this.id = id;
            this.Time = Time;

        }

        public String getTitle() {
            return name;
        }

        public int getId() {
            return id;
        }

        public Date getAuthor() {
            return Time;
        }

    }

    private void ReturnBook () {
        boolean flag = false;
        ObservableList<String> borrowData = FXCollections.observableArrayList();
        Float FinePerDay = null;
        int BorrowedDate=0;
        try {
            String qu = "SELECT * FROM member WHERE id = " + id;
            ResultSet r1 = this.handler.execQuery(qu);

            while(r1.next()) {
                FinePerDay=r1.getFloat("FinePerDay");
            }
            qu="select BorrowedDate From middle limit 1";
            r1 = this.handler.execQuery(qu);
            while(r1.next()) {
                BorrowedDate=r1.getInt("BorrowedDate");
            }
            qu = "SELECT * FROM borrow WHERE memberId = " + id ;
            ResultSet rs = this.handler.execQuery(qu);
            while(rs.next()) {
                this.isReadyForSubmission = true;
                flag = true;
                int borrowBookId = rs.getInt("bookId");
                Timestamp Ts=rs.getTimestamp("borrow_time");
                borrowData.add("borrow Date at Time : " + Ts.toString());
                qu = "SELECT * FROM book WHERE id = " + borrowBookId;
                r1 = this.handler.execQuery(qu);
                borrowData.add("");
                borrowData.add("Book Information : ");

                while (r1.next()) {
                    borrowData.add("\tBook Name : " + r1.getString("title"));
                    borrowData.add("\tBook Id : " + r1.getString("id"));
                    borrowData.add("\tBook Author : " + r1.getString("author"));
                    borrowData.add("\tBook Publisher : " + r1.getString("publisher"));
                }
                borrowData.add("\tThis book has been loaned out for: " + timeChange(Ts)+" days");
                borrowData.add("\tThe cost of your rental is: " + fanCalculation(timeChange(Ts),BorrowedDate,FinePerDay));
                if (fanCalculation(timeChange(Ts),BorrowedDate,FinePerDay)>0.1){
                    isNeedToPay=true;
                }
                borrowData.add("");
            }

            if (!flag) {
                borrowData.add("You haven't borrowed a book yet");
                this.ReturnBtn.setDisable(true);
            } else {
                this.ReturnBtn.setDisable(false);
            }

            this.RTableView.getItems().setAll(borrowData);
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

    }
    private long timeChange(Timestamp ts ){
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        Calendar reDate1 = Calendar.getInstance();
        java.util.Date dt1 = reDate1.getTime();
        long nd=1000*24*60*60;
        long diff=dt1.getTime()-ts.getTime();
        long countDays=diff/nd;
        return countDays;
    }

    private double fanCalculation (long countDays, int day,Float fan){
        if(countDays<=day){
            return fan*0;
        }else {
            return fan*(countDays-day);
        }
    }
    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage)this.bIdR.getScene().getWindow()).close();
    }

    @FXML
    private void returnButtonAction(ActionEvent event) {
        if (!this.isReadyForSubmission) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "There are no books to return", "Empty");
        } else if (this.isNeedToPay) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Please see the administrator to pay the overdue fee before returning the book", "Need pay");
        }else {
                String query = "DELETE FROM borrow WHERE bookId = " + Integer.parseInt(this.bIdR.getText()) ;
                String query2 = "UPDATE book SET number = number + 1 WHERE id = " + Integer.parseInt(this.bIdR.getText()) ;
                String qu3 = "UPDATE member SET nBorrowed = nBorrowed +1 WHERE id = " + this.id ;
                Optional<ButtonType> response = pop.showConfirmAlert("Are you sure want to submit the book ?", "Book Submission");
                if (response.get() == ButtonType.OK) {
                    if (this.handler.execAction(query) && this.handler.execUpdate(query2) && this.handler.execUpdate(qu3)) {
                        pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Submitted Successfully", "success");
                        this.bIdR.fireEvent(new ActionEvent());
                    } else {
                        pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
                    }
                } else {
                    pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Cancelled", "Cancelled");
                }


        }
    }

    @FXML
    private void Search(ActionEvent event) {
        list.clear();
        if (SBName.getText().equals("")){
            loaddata();
            initCol();
        }else {
            String qu = "SELECT * FROM book WHERE title like '" + SBName.getText() + "'";
            ResultSet result = handler.execQuery(qu);
            try {
                while (result.next()) {
                    String title = result.getString("title");
                    int id = result.getInt("id");
                    String author = result.getString("author");
                    String publisher = result.getString("publisher");
                    int number = result.getInt("number");
                    list.add(new BookList.Book(title, id, author, publisher, number));
                }

            } catch (Exception e) {
                System.out.println(e);
            }

            BTableView.getItems().setAll(list);
        }
    }

    @FXML
    private void Borrow(ActionEvent event) {
        int bookId = Integer.parseInt(this.BBid.getText());
        String qu = "SELECT * FROM book WHERE id = " + bookId ;
        ResultSet result = handler.execQuery(qu);
        String bName = null;
        int numberB =0;
        try {
            while (result.next()) {
                 numberB = result.getInt("number");
                 bName = result.getString("title");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        qu = "SELECT * FROM member WHERE id = " + this.id ;
        result = handler.execQuery(qu);
        int numberM =0;
        try {
            while (result.next()) {
                numberM = result.getInt("nBorrowed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (BBid.getText().isEmpty()) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Please select Book", "Invalid Entry");
        } else if (numberB<=0) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book is not Available", "Not Available");
        }else if (numberM<=0) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Running out of book loans", "Not Available");
        } else {
            Optional<ButtonType> response = pop.showConfirmAlert("Are you sure want to borrow the book " + bName  + " ?", "Borrow Book");
            if (response.get() == ButtonType.OK) {
                String qu1 = "INSERT INTO borrow(bookId,memberId) VALUES(" + bookId + "," + this.id + ")";
                String qu2 = "UPDATE book SET number = number-1 WHERE id = " + bookId ;
                String qu3 = "UPDATE member SET nBorrowed = nBorrowed-1 WHERE id = " + this.id ;
                if (this.handler.execAction(qu1) && this.handler.execUpdate(qu2) && this.handler.execUpdate(qu3)) {
                    pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Borrowed Successfully", "Success");
                    list.clear();
                    loaddata();
                    initCol();
                } else {
                    pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
                    list.clear();
                    loaddata();
                    initCol();
                }
            } else {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Borrow Operation Cancelled", "Cancelled");
                list.clear();
                loaddata();
                initCol();
            }

        }
    }
    @FXML
    private void ChangeTab(ActionEvent event){
        ReturnBook ();
    }



}
