package com.example.library_project.main;

import com.example.library_project.database.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class book {
    int id;
    String title;
    String author;
    String publisher;
    int number;

    public book(String title,String author,String publisher,int number)  {
        this.author=author;
        setId();
        this.title=title;
        this.number=number;
        this.publisher=publisher;
    }

    public void setId()  {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "select * from book order by id DESC limit 1;";
        ResultSet result = handler.execQuery(qu);
        while (true) {
            try {
                if (!result.next()) break;
                id = result.getInt("id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        id++;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
