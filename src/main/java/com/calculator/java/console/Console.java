package com.calculator.java.console;

import com.calculator.java.comand.Calculation;
import com.calculator.java.comand.Command;
import com.calculator.java.comand.Selection;
import com.calculator.java.console.exception.TerminationException;
import com.calculator.java.console.exception.WrongInputException;
import com.calculator.java.database.Database;

import java.io.*;
import java.util.*;

import static com.calculator.java.comand.CommandTypes.*;

public class Console {
    private final String WRONG_INPUT_MESSAGE = "잘 못된 입력입니다.\n";
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private final Validation validation;
    private final Database database;

    public Console(Validation validation, Database database) {
        this.validation = validation;
        this.database = database;
    }

    public void run() {
        while(true) {
            if(!selectCommand()) break;
        }
    }

    private boolean selectCommand() {
        try {
            showCommandType();
            String selectedCommand = input();
            Command command = getCommand(selectedCommand).orElseThrow(TerminationException::new);

            if(command instanceof Calculation) {
                String mathExpression = input();

                if(validation.validate(mathExpression)) ((Calculation) command).setMathExpression(mathExpression);
                else throw new WrongInputException(WRONG_INPUT_MESSAGE);
            }

            String result = command.doCommand();
            System.out.println(result+"\n");

        }catch (WrongInputException wrongInputException) {
          System.out.println(wrongInputException.getMessage());
        } catch (TerminationException terminationException){
            return false;
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            return false;
        }

        return true;
    }

    private void showCommandType() {
        StringBuilder sb = new StringBuilder();

        sb.append(SELECTION).append("\n")
                .append(CALCULATION).append("\n")
                .append(TERMINATION).append("\n")
                .append("\n선택 : ");

        System.out.print(sb);
    }

    private String input() throws IOException {
        String input = br.readLine().trim();
        System.out.println();
        return input;
    }

    private Optional<Command> getCommand(String selectedCommand) throws WrongInputException {
        if (selectedCommand.equals(SELECTION.getCommandId())) {
            return Optional.of(new Selection(database));
        } else if (selectedCommand.equals(CALCULATION.getCommandId())) {
            return Optional.of(new Calculation(database));
        } else if (selectedCommand.equals(TERMINATION.getCommandId())) {
            return Optional.empty();
        } else {
            throw new WrongInputException(WRONG_INPUT_MESSAGE);
        }
    }
}