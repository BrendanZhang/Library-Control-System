package com.example.library_project.main;

import com.example.library_project.database.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class people {
    int id;
    String username;
    String mobile;
    String email;
    String password;
    int nBorrowed;
    float finePerDay;

    public people(String username,String mobile,String email,String password)  {
        this.email=email;
        this.username=username;
        this.mobile=mobile;
        this.password=password;
        setId();
        setFinePerDay();
        setnBorrowed();
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public int getnBorrowed() {
        return nBorrowed;
    }

    public String getEmail() {
        return email;
    }


    public String getMobile() {
        return mobile;
    }

    public int getId() {
        return id;
    }

    public void setId() {
        int idd=0;
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "select * from member order by id DESC limit 1;";
        ResultSet result = handler.execQuery(qu);
        while (true) {
            try {
                if (!result.next()) break;
                idd = result.getInt("id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        idd++;
        this.id=idd;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFinePerDay() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "select FinePerDay from middle limit 1;";
        ResultSet result = handler.execQuery(qu);
        while (true) {
            try {
                if (!result.next()) break;
                this.finePerDay = result.getInt("FinePerDay");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setnBorrowed() {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "select nBorrowed from middle limit 1;";
        ResultSet result = handler.execQuery(qu);
        while (true) {
            try {
                if (!result.next()) break;
                this.nBorrowed = result.getInt("nBorrowed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
