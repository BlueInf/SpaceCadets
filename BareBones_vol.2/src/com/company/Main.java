package com.company;

public class Main {
    /* The interpreter supports :
            incr- increases the value by 1
            decr- decreases the value by 1
            clear- clears the value
            arithmetic expressions for example X=(7615*61)/5+9
            while loops - while x not 3 do; incr x;end;
            comments
     */


    public static void main(String[] args) {
        Interpreter i= new Interpreter("text.txt"); //Declaring and initializing the interpreter, using the text file's name
        i.readFile(); //Running the interpreter
    }
}                            