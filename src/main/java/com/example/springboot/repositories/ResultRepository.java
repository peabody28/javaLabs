package com.example.springboot.repositories;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.ResultEntity;
import com.example.springboot.interfaces.entities.IMathOperation;
import com.example.springboot.interfaces.entities.IResult;
import com.example.springboot.interfaces.repositories.IResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component(value="resultRepository")
public class ResultRepository implements IResultRepository
{
    private DbContext dbContext;

    @Autowired
    public ResultRepository(DbContext _dbContext)
    {
        dbContext = _dbContext;
    }

    public IResult Create(IMathOperation mathOperation, double result)
    {
        try {
            var sql = "INSERT INTO result (math_operation_id, value) VALUES(?, ?)";
            PreparedStatement st = dbContext.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, mathOperation.getId());
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

    public ResultEntity Object(IMathOperation mathOperation)
    {
        try
        {
            Statement statement = dbContext.conn.createStatement();

            var sql = String.format("SELECT * FROM result WHERE math_operation_id = '%d'", mathOperation.getId());
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();

            return new ResultEntity(resultSet.getInt("id"), mathOperation, resultSet.getDouble("value"));
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
