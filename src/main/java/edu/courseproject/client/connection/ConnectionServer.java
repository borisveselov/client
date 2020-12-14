package edu.courseproject.client.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionServer {
    private static ConnectionServer instance;
    private volatile static boolean instanceCreated = false;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    private String host = "127.0.0.1";
    private int port = 2525;

    private ConnectionServer() {
        System.out.println("Connecting...");

        try {
            socket = new Socket(host, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isConnected() {
        return socket != null;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static ConnectionServer getInstance() {
        if (!instanceCreated) {
            synchronized (ConnectionServer.class) {
                if (!instanceCreated) {
                    instance = new ConnectionServer();
                    instanceCreated = true;
                }
            }
        }
        return instance;
    }


}
