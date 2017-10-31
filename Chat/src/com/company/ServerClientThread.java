
package com.company;

import java.io.*;
import java.net.Socket;


public class ServerClientThread extends Thread {

    private Socket socketClient = null;
    private boolean isThreadOn = true;
    private BufferedReader in;
    private PrintWriter out;
    private String name = "";
    private Thread t;


    public ServerClientThread(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {

        try {

            System.out.println("Accepted Client Address - " + socketClient.getInetAddress().getHostName());

            DataOutputStream dout = new DataOutputStream(socketClient.getOutputStream());
            DataInputStream din = new DataInputStream(socketClient.getInputStream());

            dout.writeUTF("-*-*-*-*-What is your name ?-*-*-*-*-");
            dout.flush();

            name = din.readUTF();

            System.out.println(name+" has joined the discussion.");
            Server.broadcast(name + " has joined the discussion.", "Chatter");


            while (isThreadOn) {

                String msg = din.readUTF();
                System.out.println(name+" : "+msg);

                if (!Server.serverOn) {

                    System.out.println("Server has stopped...");
                    Server.broadcast("Server has stopped...", "Admin");
                    isThreadOn = false;
                }

                if (msg.equalsIgnoreCase("/quit")) {
                    System.out.println("Stopping client thread for client");
                    Server.broadcast(name + " has left the discussion.", "Chatter");
                    isThreadOn = false;
                    break;
                }
                Server.broadcast(msg, name);

            }

            dout.close();
            din.close();

            socketClient.close();
            Server.remove(socketClient);

            System.out.println("Stopped...");

        } catch (IOException e) {

            System.out.println("Chatter error " + e);

        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public Socket getSocket() {
        return socketClient;
    }

}