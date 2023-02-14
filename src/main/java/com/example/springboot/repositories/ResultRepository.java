package com.example.springboot.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component(value="resultRepository")
public class ResultRepository
{
    DbContext dbContext;

    @Autowired
    public ResultRepository(DbContext _dbContext)
    {
        dbContext = _dbContext;
    }
    public ResultEntity Create(MathOperationEntity mathOperation, double result)
    {
        try {
            var sql = "INSERT INTO result (math_operation_id, value) VALUES(?, ?)";
            PreparedStatement st = dbContext.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, mathOperation.id);
            st.setDouble(2, result);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if(rs.next())
                return new ResultEntity(rs.getInt(1), mathOperation, result);
            return null;
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
