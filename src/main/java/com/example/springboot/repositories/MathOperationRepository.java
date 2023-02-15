package com.example.springboot.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.OperationEntity;
import com.example.springboot.entities.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component(value="mathOperationRepository")
public class MathOperationRepository{

    DbContext dbContext;

    @Autowired
    public MathOperationRepository(DbContext _dbContext)
    {
        dbContext = _dbContext;
    }

    public MathOperationEntity Create(double first, double second, OperationEntity operationEntity)
    {
        try {
            var sql = "INSERT INTO mathOperation (first, second, operation_id) VALUES(?, ?, ?)";
            PreparedStatement st = dbContext.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setDouble(1, first);
            st.setDouble(2, second);
            st.setInt(3, operationEntity.id);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if(rs.next())
                return new MathOperationEntity(rs.getInt(1), first, second, operationEntity);
            return null;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public MathOperationEntity Object(int id)
    {
        try
        {
            Statement statement = dbContext.conn.createStatement();

            var sql = String.format("SELECT * FROM mathOperation WHERE id = '%d'", id);
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();

            return new MathOperationEntity(resultSet.getInt("id"), resultSet.getDouble("first"),
                    resultSet.getDouble("second"), null);
        }
        catch (Exception ex)
        {
            return null;
        }
    }


}
