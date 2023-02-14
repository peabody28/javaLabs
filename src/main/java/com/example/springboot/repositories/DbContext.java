package com.example.springboot.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component(value="dbContext")
@Scope("singleton")
public class DbContext {
    public Connection conn;

    public DbContext()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://peabody28.com:6612/mathOperation", "root", "152437689hH#");
        }
        catch (Exception ex)
        {

        }
    }
}
