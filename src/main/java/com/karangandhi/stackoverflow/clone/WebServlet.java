package com.karangandhi.stackoverflow.clone;

import com.karangandhi.stackoverflow.clone.Routes.Routes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

// TODO: turn this into a tcp and write http server in c++
public class WebServlet {
    static final boolean verbose = true;
    public static Thread thread = null;

    public static void bind(final int PORT) {
//        thread = new Thread(new Runnable() { // run the socket server on another thread to prevent the blocking of the main thread!
//            @Override
//            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(PORT);
                    System.out.println("[ServerSocket] Successful connected to port " + PORT);

                    while(true) createSocket(serverSocket.accept());
                } catch (IOException exception) {
                    System.out.println("[ServerSocket] ERROR: " + exception.getMessage());
                }
//            }
//        });
//        thread.start();
    }

    static void createSocket(Socket socket) {
        if (verbose) System.out.println("[Socket] Connection opened at " + new Date());
        Thread thread = new Thread(new Routes(socket, verbose));
        thread.start();
    }
}
