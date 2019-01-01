package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private final int port;
    private boolean isRunning = false;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        if(!isRunning) {
            isRunning = true;
            try {
                listen();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void listen() throws IOException {
        Executor executor = Executors.newCachedThreadPool();
        ServerSocket welcomingSocket = new ServerSocket(port);
        Socket connectionSocket = null;

        while(true) {
            try {
                connectionSocket = welcomingSocket.accept();
                Handler handler = new Handler(connectionSocket);
                executor.execute(handler);

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] argv) {
        Server server = new Server(8080);
        server.start();
    }
}
