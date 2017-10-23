package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;


public class Interpreter {
    private String file_name;

    private Hashtable<String, Integer> hashtable = new Hashtable<String, Integer>();// Dynamic data structure- Hashtable to store the variables
    private StringBuilder text = new StringBuilder();//Usage of StringBuilder to manipulate the input text
    private String[] commandsClone = new String[]{};//Clone String array that contains the commands
    private int counterEnd;//Variable that we use to know where are we in the input text

    public Interpreter(String file_name) {
        this.file_name = file_name;
    }//Our Constructor for the Interpreter

    //Read file method that interprets the BareBones Language and executes it
    public void readFile() {
        String tmpLine = null;
        try {

            FileReader fr = new FileReader(file_name);
            BufferedReader br = new BufferedReader(fr);
            while ((tmpLine = br.readLine()) != null) text.append(tmpLine + '\n');
            br.close();

            String codeToCompile = new String(text.toString());//Parse the StringBuilder to String
            String command;//String that holds a Command

            int counter = -1;//Counter to know what is the index of the command

            codeToCompile = removeComments(codeToCompile);// Removing the comments from the code to be compiled

            System.out.println(codeToCompile);

            String[] commands = codeToCompile.split("(;)");//Splitting the input code line by line
            commandsClone = commands.clone(); //Cloning the commands

            for (int i = 0; i < commands.length; i++) {//Iterating through the commands
                counter++; //Counter to know at what line are we
                command = commands[i].trim(); //We remove all the spaces before and after the string

                if (command.matches("[a-zA-Z]*(=)[0-9A-Za-z]*( ){0,}([+-/*]( ){0,}[0-9A-Za-z]*( ){0,})*")) {//Seeing if we have an arithmetic expression
                    arithmeticExpression(command);//execute arithmetic expression

                    continue;//Iterate through the next command
                }

                isSyntaxCorrect(command);//Looking if the syntax is correct

                if (isLoop(command)) {  //If it is a loop we execute the commands within the loop cycle
                    String temporaryLine[] = command.split(" ");//Split the string into tokens
                    int limit = Integer.parseInt(temporaryLine[3]);//find the limitValue for the while loop
                    loop(temporaryLine[1], limit, counter); //We call the loop method
                    i = counterEnd;
                    command = commands[i]; // Now, the line we read is the line after the last command of the while loop
                }
                if (command.contains("end")) continue;// We skip the end command

                String syntaxArray[] = command.split(" ");// Splitting the commands
                System.out.println("Current bare bones sentence is: " + command);
                executeCommand(syntaxArray[0], syntaxArray[1]); //Executing the command
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Method that executes our Command
    private void executeCommand(String command, String variable) {
        switch (command) { //Looking through the different commands, we always see if we have the variable in the hashtable
            //If we do not have it we put it
            case "incr"://Case incr add 1 to the variable
                if (hashtable.containsKey(variable)) {
                    hashtable.put(variable, hashtable.get(variable) + 1);
                    System.out.println(hashtable);
                } else {
                    hashtable.put(variable, 1);
                    System.out.println(hashtable);
                }
                break;
            case "clear"://Case clear the value of the variable is 0
                if (hashtable.containsKey(variable)) {
                    hashtable.put(variable, 0);
                    System.out.println(hashtable);
                } else {
                    hashtable.put(variable, 0);
                    System.out.println(hashtable);
                }
                break;
            case "decr"://Case decr the value of the variable decreases by 1
                if (hashtable.containsKey(variable)) {
                    hashtable.put(variable, hashtable.get(variable) - 1);
                    System.out.println(hashtable);
                } else {
                    hashtable.put(variable, -1);
                    System.out.println(hashtable);
                }
                break;
        }
    }


    private void loop(String variable, Integer limit, int fromLine) { //Implementation of the while loop
        int counterIndex = fromLine + 1;//Counter that is for which line are we, it is from line + 1, because we need the next line after the while loop
        String currentLine;
        while (true) {
            currentLine = commandsClone[counterIndex].trim(); // Remove spaces before and after the current line
            if (currentLine.matches("(while) [A-Za-z]+ not [0-9]+ do")) { //If  we have a while loop
                String temporaryLine[] = currentLine.split(" ");
                int limitValue = Integer.parseInt(temporaryLine[3]);
                loop(temporaryLine[1], limitValue, counterIndex); //Recursive calling if we find another while loop
                counterIndex = counterEnd + 1;
            }
            else if(currentLine.matches("[a-zA-Z]*(=)[0-9A-Za-z]*( ){0,}([+-/*]( ){0,}[0-9A-Za-z]*( ){0,})*")){//Seeing if we have an arithmetic expression
                arithmeticExpression(currentLine);//Execute the arithmetic expression
                counterIndex++;//Iterate through the next command
                counterEnd=counterIndex;
            }
            else if (!currentLine.matches("(while) [A-Za-z]+ not [0-9]+ do|(end)")) { //If it is a command and not an arithmetic expression
                isSyntaxCorrect(currentLine);
                String[] lineToRun = currentLine.split(" ");
                System.out.println("Current bare bones sentence is: " + currentLine);
                executeCommand(lineToRun[0], lineToRun[1]);//Execute the command
                counterIndex++;//Counter increases by one
                counterEnd = counterIndex;
            } else if ((currentLine.matches("end")) && (hashtable.get(variable) == limit)) break;//If we have an end command and we have finished or loop,break from the while loop
            else {
                counterIndex = fromLine + 1;
            }
        }
    }


    //Syntax correctness checker
    private void isSyntaxCorrect(String line) {
        if (!line.matches("((incr|decr|clear) [A-Za-z]+)|((while) [A-Za-z]+ not [0-9]+ do)|(end)")) {//If line does not match any of the patterns, we exit from the program
            System.out.println("Syntax Error in line: [" + line + "]");
            System.out.println("System Exit");
            System.exit(0);
        }
    }

    private void arithmeticExpression(String line) {//Method that executes our arithmetic expression
        System.out.println("Current bare bones sentence is: " + line);
        String[] expressionArray = line.split("(=)");//Splitting the string into two --> 1)left side before '=' sign and 2)right side after '='
        String variable = expressionArray[0];
        if (hashtable.containsKey(variable)) {// Seeing if we have the variable
            hashtable.put(variable, evaluate(expressionArray[1]));
            System.out.println(hashtable);
        } else {
            hashtable.put(variable, evaluate(expressionArray[1]));
            System.out.println(hashtable);
        }

    }

    private int evaluate(String expression) { //Method that evaluates our right side of the expression
        char[] tokens = expression.toCharArray();
        // Stack for numbers: 'values'
        Stack<Integer> values = new Stack<Integer>();

        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            // Current token is a whitespace, skip it
            if (tokens[i] == ' ')
                continue;

            // Current token is a number, push it to stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9'))
                    sbuf.append(tokens[i++]);
                values.push(Integer.parseInt(sbuf.toString()));
                i = i - 1;
            }
            // Current token is a digit, push it to stack for digit
            else if ((tokens[i] >= 'A' && tokens[i] <= 'Z') || ((tokens[i] >= 'a' && tokens[i] <= 'z'))) {
                StringBuffer sbuf = new StringBuffer();
                // There may be more than one digits in number
                while (i < tokens.length && ((tokens[i] >= 'a' && tokens[i] <= 'z') || ((tokens[i] >= 'A' && tokens[i] <= 'Z')))) {
                    sbuf.append(tokens[i++]);
                }
                values.push(hashtable.get(sbuf.toString()));
                i = i - 1;
            }

            // Current token is an opening brace, push it to 'ops'
            else if (tokens[i] == '(')
                ops.push(tokens[i]);

                // Closing brace encountered, solve entire brace
            else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            }

            // Current token is an operator.
            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == '*' || tokens[i] == '/') {
                // While top of 'ops' has same or greater precedence to current
                // token, which is an operator. Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        // Top of 'values' contains result, return it
        return values.pop();
    }


    // Returns true if 'op2' has higher or same precedence as 'op1',
    // otherwise returns false.
    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    // A utility method to apply an operator 'op' on operands 'a'
    // and 'b'. Return the result.
    private int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    //Method that checks if we have a loop
    private boolean isLoop(String line) {
        if (line.matches("(while) [A-Za-z]+ not [0-9]+ do")) return true;
        else return false;
    }

    //Method that removes all comments
    private String removeComments(String line) {
        line = line.replaceAll("((['\"])(?:(?!\\2|\\\\).|\\\\.)*\\2)|\\/\\/[^\\n]*|\\/\\*(?:[^*]|\\*(?!\\/))*\\*\\/", "");
        line = line.replaceAll("\n", "");
        return line;
    }
}

/*
//Sample program that multiplies two numbers
clear X;
incr X; //First number
incr X;
incr X;
incr X;
X=X+2;
clear Y;
incr Y; //Second number
incr Y;
incr Y;
incr Y;
clear Z; //Product of the two numbers
while X not 0 do;
   clear W;
   while Y not 0 do;
      incr Z;
      incr W;
      decr Y;
   end;
   while W not 0 do;
      incr Y;
      decr W;
   end;
   decr X;
end;

 */
//Made by Krasimir Marinov 22.10.2017