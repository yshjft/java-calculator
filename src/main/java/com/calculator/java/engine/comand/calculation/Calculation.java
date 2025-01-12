package com.calculator.java.engine.comand.calculation;


import com.calculator.java.database.Database;
import com.calculator.java.engine.comand.Commandable;
import com.calculator.java.global.Enum.Operators;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Calculation implements Commandable {
    private final String IMPOSSIBLE = "계산할 수 없습니다.";

    private final MathExpression mathExpression;
    private final Database database;

    public Calculation(MathExpression mathExpression, Database database) {
        this.mathExpression = mathExpression;
        this.database = database;
    }

    @Override
    public String doCommand() {
        List<String> elements = new ArrayList<>();

        if(!multiplyAndDivide(elements)) {
            return IMPOSSIBLE;
        }else{
            String result = handleResultFormat(plusAndMinus(elements));

            saveResult(result);

            return result;
        }
    }

    private boolean multiplyAndDivide(List<String> elements) {
        StringTokenizer st = new StringTokenizer(mathExpression.getMathExpression());
        int numOfElements = st.countTokens();

        for (int i = 0; i < numOfElements; i++) {
            String element = st.nextToken();

            if (i % 2 == 0) {
                int indexOfLastElement = elements.size() - 1;
                if (i > 0 && (elements.get(indexOfLastElement).equals("*") || elements.get(indexOfLastElement).equals("/"))) {
                    String operator = elements.remove(indexOfLastElement);

                    double num1 = Double.parseDouble(elements.remove(indexOfLastElement - 1));
                    double num2 = Double.parseDouble(element);

                    if(operator.equals("/") && num2 == 0) return false;

                    element = Double.toString(calculate(num1, num2, operator));
                }
            }

            elements.add(element);
        }

        return true;
    }

    private double plusAndMinus(List<String> elements) {
        int numOfElements = elements.size();
        double result = Double.parseDouble(elements.get(0));

        for (int i = 2; i < numOfElements; i += 2) {
            double num = Double.parseDouble(elements.get(i));
            String operator = elements.get(i - 1);
            result = calculate(result, num, operator);
        }

        return result;
    }

    private double calculate(double num1, double num2, String operator) {
        return switch (Operators.findOperator(operator)) {
            case PLUS -> num1 + num2;
            case MINUS -> num1 - num2;
            case MULTIPLICATION -> num1 * num2;
            default -> num1 / num2;
        };
    }

    private String handleResultFormat(double calculationResult) {
        String result = String.format("%.1f", calculationResult);

        if(result.contains(".0")) {
            return result.replace(".0", "");
        }else {
            return result;
        }
    }

    private void saveResult(String result) {
        database.add(mathExpression.getMathExpression(), result);
    }
}
