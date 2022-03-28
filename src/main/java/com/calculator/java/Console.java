package com.calculator.java;

import com.calculator.java.engine.comand.CommandTypes;
import com.calculator.java.exception.WrongInputException;

import java.io.*;
import java.util.*;

public class Console {
    private final String WRONG_INPUT_MESSAGE = "잘 못된 입력입니다.";
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final Validation validation;

    public Console(Validation validation) {
        this.validation = validation;
    }

    public void showCommandType() {
        StringBuilder sb = new StringBuilder();

        Arrays.asList(CommandTypes.values())
                .forEach(commandType -> sb.append(commandType).append("\n"));
        sb.append("\n선택 : ");

        System.out.print(sb);
    }

    public String inputCommandType() throws IOException, WrongInputException{
        String selectedCommand = br.readLine().trim();
        System.out.println();

        if(!validation.validateSelectedCommand(selectedCommand)) {
            throw new WrongInputException(WRONG_INPUT_MESSAGE);
        }

        return selectedCommand;
    }

    public String inputMathExpression() throws IOException, WrongInputException{
        String mathExpression = br.readLine().trim();

        if(validation.validateMathExpression(mathExpression)) return mathExpression;
        else throw new WrongInputException(WRONG_INPUT_MESSAGE);
    }

    public void output(String result) {
        System.out.println(result+"\n");
    }
}
