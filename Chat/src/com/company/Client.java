package com.company;



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 */
public class Client {

    private static Socket s = null;
    private static DataInputStream din;
    private static DataOutputStream dout;
    private static int uniqueID;



    public Client() {
        try {
            s = new Socket("127.0.0.1", 1432);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {

        Client n = new Client();
        try {
            String messageIn = "";
            String messageOut = "";

            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());


            new Thread(new ReadInput(dout)).start();
            new Thread(new Output(din)).start();



        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


    }
}


 class ReadInput extends Thread{
    private Thread t=null;
    private DataOutputStream dout;

     public ReadInput(DataOutputStream dout){
         this.dout=dout;
     }

     @Override
     public void run() {
         try {
             BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
             String  msg="";

             System.out.println("-*-*-*-*-Hello to the Battleship-*-*-*-*-");
             System.out.println("-*-*-*-*-*-To exit type /quit-*-*-*-*-*-");

             while (true) {
                 msg=br.readLine();
                 dout.writeUTF(msg);
                 dout.flush();
             }
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     public void start () {
         if (t == null) {
             t = new Thread(this);
             t.start();
         }
     }
}


class Output extends Thread{
    private Thread t=null;
    private DataInputStream din;

    public Output(DataInputStream din){
        this.din=din;
    }

    @Override
    public void run() {
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
            String  msg="";

            while (true) {

                msg=din.readUTF();
                System.out.println(msg);

            }
        }catch(Exception e){
            System.out.println("-*-*-*-You decide to leave-*-*-*-");
            System.exit(-1);
            //e.printStackTrace();
        }
    }
    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}


