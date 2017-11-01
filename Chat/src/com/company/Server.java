package com.company;

import java.io.PrintWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 */
public class Server extends Thread {

    ServerSocket serverSocket;
    public static boolean serverOn = true;
    public static ArrayList<ServerClientThread> clientList = new ArrayList<>();
    public Server() {
        try {
            //Start listening on the port
            serverSocket = new ServerSocket(1432);

        } catch (IOException ioe) {

            System.out.println("The server could not be created on port 1432");
            System.exit(-1);

        }

        while (true) {

            try {

                System.out.println("Server waiting for connection requests...");
                Socket s = serverSocket.accept();
                ServerClientThread currentThread = new ServerClientThread(s);
                currentThread.start();
                clientList.add(currentThread);

            } catch (IOException ioe) {

                System.out.println("Exception was found");
                ioe.printStackTrace();

            }
        }
    }


    static synchronized void broadcast(String message, String name) throws IOException {

        // Sends the message to every client including the sender.
        DataOutputStream dout;
        Socket s;

        for (int i = 0; i < clientList.size(); i++) {
            s = clientList.get(i).getSocket();
            dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(name+" : "+message);
            dout.flush();

        }
    }

    static synchronized void remove(Socket s) {

        for(int i=0;i < clientList.size(); i++){

            if(clientList.get(i).getSocket()==s){

                clientList.remove(i);

            }
        }
    }

    public static void main(String[] args) throws IOException {

        new Server();

    }

}
