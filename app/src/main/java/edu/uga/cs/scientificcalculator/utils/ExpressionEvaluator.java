package edu.uga.cs.scientificcalculator.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExpressionEvaluator {

    public static double evaluate(String expr) throws Exception {
        // exp4j supports built-in functions like sin, cos, tan, log, ln, sqrt, etc.
        Expression expression = new ExpressionBuilder(expr)
                .build();

        return expression.evaluate();
    }
}