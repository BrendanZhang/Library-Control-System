package com.example.library_project.main;

import com.example.library_project.alert.pop;
import com.example.library_project.database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class staff implements Initializable {
    DatabaseHandler handler;
    @FXML
    private Text bName;
    @FXML
    private Text bAuthor;
    @FXML
    private Text bNumber;
    @FXML
    private Text mName;
    @FXML
    private Text mMobile;
    @FXML
    private TextField bId;
    @FXML
    private TextField mId;
    @FXML
    private TextField memberId;
    @FXML
    private TextField RBookID;
    @FXML
    private ListView<String> issueDataList;
    Boolean isReadyForSubmission = false;
    @FXML
    private Button renewBtn;
    @FXML
    private Button submissionBtn;
    @FXML
    private Button issueBtn;
    Boolean isBookInStock = false;
    boolean isBookAvailable = false;
    boolean isAvailableMember = false;
    boolean isNumBorrowed = false;

    public staff() {
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        this.handler = DatabaseHandler.getInstance();
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        this.loadWindow("newMember.fxml", "Add New Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        this.loadWindow("newBook.fxml", "Add New Book");
    }

    @FXML
    private void loadViewMember(ActionEvent event) {
        this.loadWindow("MemberList.fxml", "Member List");
    }

    @FXML
    private void loadViewBook(ActionEvent event) {
        this.loadWindow("BookList.fxml", "Book List");
    }

    @FXML
    private void loadSettings(ActionEvent event) {
        this.loadWindow("Setting.fxml", "Setting");
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
    private void showBookInfo(ActionEvent event) {
        boolean flag = false;
        this.isBookInStock = false;
        String qu = "SELECT * FROM book where id= " + this.bId.getText() ;
        ResultSet rs = this.handler.execQuery(qu);

        try {
            while(rs.next()) {
                this.bName.setText(rs.getString("title"));
                this.bAuthor.setText(rs.getString("author"));
                this.bNumber.setText(String.valueOf(rs.getInt("number")));
                flag = true;
                this.isBookInStock = true;
                if (rs.getInt("number")>0){
                    this.isBookAvailable=true;
                }
            }

            if (!flag) {
                pop.clearText(Arrays.asList(this.bName, this.bNumber));
                this.bAuthor.setText("No Such Book Available");
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        mId.requestFocus();
    }

    @FXML
    private void showMemberInfo(ActionEvent event) {
        boolean flag = false;
        this.isAvailableMember = false;
        String qu = "SELECT * FROM member where id= " + Integer.parseInt(this.mId.getText());
        ResultSet rs = this.handler.execQuery(qu);

        try {
            while(rs.next()) {
                this.mName.setText(rs.getString("username"));
                this.mMobile.setText(rs.getString("mobile"));
                flag = true;
                this.isAvailableMember = true;
                if (rs.getInt("nBorrowed")>0){
                    this.isNumBorrowed=true;
                }
            }

            if (!flag) {
                this.mMobile.setText("");
                this.mName.setText("No Such Member Available");
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }
        issueBtn.requestFocus();
    }

    @FXML
    private void issueBook(ActionEvent event) {
        if (!this.isBookInStock) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Please select Book", "Invalid Entry");
        } else if (!this.isAvailableMember) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Please select Member", "Invalid Entry");
        } else if (!this.isBookAvailable) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book is not Available", "Not Available");
        } else if (!this.isNumBorrowed) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Running out of book lending times", "Not Available");
        }else {
            Optional<ButtonType> response = pop.showConfirmAlert("Are you sure want to borrow the book " + this.bName.getText() + " \nto " + this.mName.getText() + " ?", "Borrow Book");
            if (response.get() == ButtonType.OK) {
                int bookId = Integer.parseInt(this.bId.getText());
                int memberId = Integer.parseInt(this.mId.getText());
                String qu1 = "INSERT INTO borrow(bookId,memberId) VALUES(" + bookId + "," + memberId + ")";
                String qu2 = "UPDATE book SET number = number-1 WHERE id = " + bookId ;
                String qu3 = "UPDATE member SET nBorrowed = nBorrowed-1 WHERE id = " + memberId ;
                if (this.handler.execAction(qu1) && this.handler.execUpdate(qu2) && this.handler.execUpdate(qu3)) {
                    pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Borrowed Successfully", "Success");
                    this.isBookInStock = this.isBookAvailable = this.isAvailableMember = false;
                    this.resetInput();
                } else {
                    pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
                    this.resetInput();
                }
            } else {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Borrow Operation Cancelled", "Cancelled");
                this.resetInput();
            }

        }
    }

    private void resetInput() {
        this.bId.setText("");
        this.bName.setText("Book Name");
        this.bAuthor.setText("Author");
        this.bNumber.setText("Number");
        this.mId.setText("");
        this.mName.setText("Member Name");
        this.mMobile.setText("Mobile");
    }

    @FXML
    private void loadBookInfo(ActionEvent event) {
        boolean flag = false;
        ObservableList<String> borrowData = FXCollections.observableArrayList();
        Float FinePerDay = null;
        int BorrowedDate = 0;
        try {
            String qu = "SELECT * FROM member WHERE id = " + Integer.parseInt(this.memberId.getText());
            ResultSet r1 = this.handler.execQuery(qu);
            borrowData.add("Member Information : ");

            while(r1.next()) {
                borrowData.add("\tMember Name : " + r1.getString("username"));
                borrowData.add("\tMember Mobile : " + r1.getString("mobile"));
                borrowData.add("\tMember Email : " + r1.getString("email"));
                FinePerDay=r1.getFloat("FinePerDay");
            }
            qu="select BorrowedDate From middle limit 1";
            r1 = this.handler.execQuery(qu);
            while(r1.next()) {
                BorrowedDate=r1.getInt("BorrowedDate");
            }
            borrowData.add("");
            qu = "SELECT * FROM borrow WHERE memberId = " + Integer.parseInt(this.memberId.getText()) ;
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
                borrowData.add("");
            }


            if (!flag) {
                borrowData.add("This member did not borrow any books");
                this.renewBtn.setDisable(true);
                this.submissionBtn.setDisable(true);
            } else {
                this.renewBtn.setDisable(false);
                this.submissionBtn.setDisable(false);
            }

            this.issueDataList.getItems().setAll(borrowData);
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

    }

    @FXML
    private void renewButtonAction(ActionEvent event) {
        if (!this.isReadyForSubmission) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "There is no book to renew", "Empty");
        } else {
            String query = "UPDATE borrow SET borrow_time = CURRENT_TIMESTAMP WHERE memberId = " + Integer.parseInt(this.memberId.getText()) +" and bookId = "+Integer.parseInt(this.RBookID.getText()) ;
            Optional<ButtonType> response = pop.showConfirmAlert("Are you sure want to renew the book ?", "Renew Book");
            if (response.get() == ButtonType.OK) {
                if (this.handler.execUpdate(query)) {
                    pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Renew Successfully", "Success");
                    this.memberId.fireEvent(new ActionEvent());
                } else {
                    pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
                }
            } else {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Cancelled", "Cancelled");
            }

        }
    }

    @FXML
    private void submissionButtonAction(ActionEvent event) {
        if (!this.isReadyForSubmission) {
            pop.showSimpleAlert(Alert.AlertType.INFORMATION, "There are no books to return", "Empty");
        } else {
            String query = "DELETE FROM borrow WHERE bookId = " + Integer.parseInt(this.RBookID.getText()) ;
            String query2 = "UPDATE book SET number = number + 1 WHERE id = " + Integer.parseInt(this.RBookID.getText()) ;
            String qu3 = "UPDATE member SET nBorrowed = nBorrowed+1 WHERE id = " + Integer.parseInt(this.memberId.getText()) ;
            Optional<ButtonType> response = pop.showConfirmAlert("Are you sure want to submit the book ?", "Book Submission");
            if (response.get() == ButtonType.OK) {
                if (this.handler.execAction(query) && this.handler.execUpdate(query2) && this.handler.execUpdate(qu3)) {
                    pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Book Submitted Successfully", "success");
                    this.RBookID.fireEvent(new ActionEvent());
                } else {
                    pop.showSimpleAlert(Alert.AlertType.ERROR, "Failed", "Failed");
                }
            } else {
                pop.showSimpleAlert(Alert.AlertType.INFORMATION, "Cancelled", "Cancelled");
            }

        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage)this.bName.getScene().getWindow()).close();
    }

    private long timeChange(Timestamp ts ){
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        Calendar reDate1 = Calendar.getInstance();
        Date dt1 = reDate1.getTime();
        long nd=1000*24*60*60;
        long diff=dt1.getTime()-ts.getTime();
        long countDays=diff/nd;
        return countDays;
    }

    private Float fanCalculation (long countDays, int day,Float fan){
        if(countDays<=day){
            return fan*0;
        }else {
            return fan*(countDays-day);
        }
    }
}
