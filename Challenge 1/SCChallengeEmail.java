package com.company;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class Main {
    
    public static void main(String[] args) {
        // write your code here
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            //String that contains the email id
            String input=br.readLine();
            //String that concatenates two strings
            String webAdress="http://www.ecs.soton.ac.uk/people/"+input;
            //String that creates an url object from the web adress
            URL myURL = new URL(webAdress);
            BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));
            String inputLine;
            String tmpLine = null;
            while ((inputLine = in.readLine()) != null ) {
                if(inputLine.contains("\"name\""))tmpLine=inputLine;
                //System.out.println(inputLine);
            }
            in.close();
            tmpLine=tmpLine.substring(tmpLine.lastIndexOf("\"name\">")+7);
            tmpLine=tmpLine.substring(0,tmpLine.lastIndexOf("</h1"));
            System.out.println(tmpLine);
            //System.out.println(tmpLine.substring(tmpLine.lastIndexOf("\"name\">") + 7));
            //System.out.println(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

