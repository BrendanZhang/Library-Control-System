package com.example.library_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;

    private static final String DB_URL = "com.mysql.cj.jdbc.Driver";
    private static Connection conn;
    private static Statement stmt;

    private DatabaseHandler() {
        createConnection();
        // CREATE BOOK TABLE
        String sql = "CREATE TABLE IF NOT EXISTS book(id int PRIMARY KEY,title VARCHAR(200),author VARCHAR(200),publisher VARCHAR(200),number int) ";
        execAction(sql);
        // CREATE MEMBER TABLE
        sql = "CREATE TABLE IF NOT EXISTS member(id int PRIMARY KEY,username VARCHAR(200),mobile VARCHAR(20),email VARCHAR(50),password VARCHAR(20),nBorrowed int,FinePerDay FLOAT) ";
        execAction(sql);
        // CREATE ISSUE TABLE
        sql = "CREATE TABLE IF NOT EXISTS borrow(bookId int PRIMARY KEY, memberId int,borrow_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY (bookId) REFERENCES book(id),FOREIGN KEY (memberId) REFERENCES member(id) )";
        execAction(sql);
        sql = "CREATE TABLE IF NOT EXISTS middle(id int PRIMARY KEY, nBorrowed int, FinePerDay Double,BorrowedDate int )";
        execAction(sql);
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    void createConnection() {
        try {
            Class.forName(DB_URL);
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/library?serverTimezone=GMT%2B8&useSSL=false&useUnicode=true&characterEncoding=UTF-8", "root", "123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (Exception e) {
            System.out.println("Exception at ExecQuery : dataHandler " + e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public boolean execAction(String query) {
        try {
            stmt = conn.createStatement();
            stmt.execute(query);
            return true;
        } catch (Exception e) {
            System.out.println("Exception at ExecQuery : dataHandler " + e.getLocalizedMessage());
            return false;
        }
    }

    public boolean execUpdate(String query) {
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.out.println("Exception at ExecQuery : dataHandler " + e.getLocalizedMessage());
            return false;
        }
    }
}
