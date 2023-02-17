package com.example.springboot;

import com.example.springboot.entities.MathOperationEntity;
import com.example.springboot.entities.OperationEntity;
import com.example.springboot.enums.Operation;
import com.example.springboot.interfaces.operations.IMathOperationOperation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(classes = Application.class)
class MathOperationTest {

    @Autowired
    IMathOperationOperation mathOperation;

    @ParameterizedTest
    @MethodSource("mathOperationCompute")
    void contextLoads(double a, double b, Operation op) {
        // Arrange
        var operationEntity = new OperationEntity(0, op.toString());
        var entity = new MathOperationEntity(0, a,b,operationEntity);

        // Act
        var result = mathOperation.Compute(entity);

        // Assert
        if(a > 0 && b > 0 && op.equals(Operation.Addition))
            assertTrue(result > 0);

        if(a < b && op.equals(Operation.Subtraction))
            assertTrue(result < 0);
    }

    private static Stream<Arguments> mathOperationCompute() {
        return Stream.of(
                arguments(1.,2., Operation.Addition),
                arguments(1.,2., Operation.Subtraction)
        );
    }
}
