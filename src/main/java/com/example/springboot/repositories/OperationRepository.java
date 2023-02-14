package com.example.springboot.repositories;

import com.example.springboot.entities.OperationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Statement;

@Component(value="operationRepository")
public class OperationRepository {

    DbContext dbContext;

    @Autowired
    public OperationRepository(DbContext _dbContext)
    {
        dbContext = _dbContext;
    }

    public OperationEntity Object(String name)
    {
        try
        {
            Statement statement = dbContext.conn.createStatement();

            var sql = String.format("SELECT * FROM operation WHERE name = '%s'", name);
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();

            return new OperationEntity(resultSet.getInt("id"), resultSet.getString("name"));
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
