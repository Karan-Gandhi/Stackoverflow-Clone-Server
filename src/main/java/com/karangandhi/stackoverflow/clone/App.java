package com.karangandhi.stackoverflow.clone;

import com.karangandhi.stackoverflow.clone.Services.FirebaseAuthService;
import com.karangandhi.stackoverflow.clone.Services.FirebaseService;
import com.karangandhi.stackoverflow.clone.Services.FirestoreService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class App {
//    public static void main(String[] args) {
//        System.out.println("Entered the server");
//        Map<String, String> data = System.getenv();
//        while (true) { }
//        System.out.println(System.getenv("PORT"));
//        WebServlet.bind(Integer.parseInt(String.valueOf(System.getenv("PORT"))));
//        try {
//            FirebaseService.InitializeApp();
//            FirestoreService.Init();
//            FirebaseAuthService.Init();
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        } catch (InterruptedException exception) {
//            exception.printStackTrace();
//        } catch (ExecutionException exception) {
//            exception.printStackTrace();
//        }


//    }

//    public static void createServer() {

//    }

    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "src/views/index.html";

    static int PORT = 3000;
    static final boolean verbose = true;

    public static void main(String[] args) {
        try {
            // System.out.println(System.getenv("PORT"));
            // PORT = Integer.parseInt(String.valueOf(System.getenv("PORT") == null ? "5000" : System.getenv("PORT")));
            System.out.println(args.length);
            PORT = Integer.parseInt(args[args.length - 1]);
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("[ServerSocket] Sucessful connected to port " + PORT);

//            new Thread(() -> {
                while(true) {
                    // This will wait till a client joins and then it will allot a sepreate thread for the given socket connection.
                    try {
                        createSocket(serverSocket.accept());
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
//            }).start();
        } catch (IOException exception) {
            System.out.println("[ServerSocket] ERROR: " + exception.getMessage());
        }
    }

    static void createSocket(Socket socket) {
        if (verbose) System.out.println("[Socket] Connection opened at " + new Date());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String fileRequested = null;
                BufferedReader inBufferedReader = null;
                PrintStream outPrintStream = null;
                BufferedOutputStream dataOut = null;

                try {
                    inBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    outPrintStream = new PrintStream(socket.getOutputStream());
                    dataOut = new BufferedOutputStream(socket.getOutputStream());

                    StringTokenizer stringTokenizer = new StringTokenizer(inBufferedReader.readLine());
                    String method = stringTokenizer.nextToken().toUpperCase();
//                    String fileRequest = stringTokenizer.nextToken().toLowerCase();
                    String fileRequest = "/";

                    if (!method.equals("GET") && !method.equals("HEAD")) {
                        if (verbose) System.out.println("[Socket] 501 Method not Implemented: " + method);

                        // TODO: Create a METHOD_NOT_SUPPORTED file
                        File file = new File(WEB_ROOT, DEFAULT_FILE);
                        int fileLength = (int) file.length();
                        String contentType = "text/html";
                        byte[] fileData = readFile(file, fileLength);

                        // Set HTTP Headers
                        outPrintStream.println("HTTP/1.1 501 Not Implemented");
                        outPrintStream.println("Server: Java HTTP Server from Karan Gandhi : 1.0");
                        outPrintStream.println("Date: " + new Date());
                        outPrintStream.println("Content-type: " + contentType);
                        outPrintStream.println("Content-length: " + fileLength);
                        // line between the headers and the body
                        outPrintStream.println();
                        outPrintStream.flush();
                        // Send the file
                        dataOut.write(fileData, 0, fileLength);
                        dataOut.flush();
                    } else {
//                        if (fileRequest.endsWith("/")) fileRequest += DEFAULT_FILE;
//                        File file = new File(WEB_ROOT, "/" + DEFAULT_FILE);
//                        int fileLength = (int) file.length();
//                        String content = getContentType(fileRequest);

                        if (method.equals("GET")) {
//                            byte[] fileData = readFile(file, fileLength);

                            // send HTTP Headers
                            outPrintStream.println("HTTP/1.1 200 OK");
                            outPrintStream.println("Server: Java HTTP Server from Karan Gandhi : 1.0");
                            outPrintStream.println("Date: " + new Date());
//                            outPrintStream.println("Content-type: " + content);
                            outPrintStream.println("Content-type: " + "text/plain");
                            outPrintStream.println("Content-length: " + 12);
                            outPrintStream.println();
                            outPrintStream.println("HELLO, WORLD");
                            outPrintStream.flush();

//                            dataOut.write(fileData, 0, fileLength);
//                            dataOut.flush();
                        }

                        if (verbose) System.out.println("[SocketRequest] File Requested " + fileRequest);
                    }

                } catch(FileNotFoundException exception) {
                    // TODO: Send the 404 file
                    System.out.println("[Socket] ERROR(FileNotFound): " + exception.getMessage());
                } catch (IOException exception) {
                    System.out.println("[Socket] ERROR: " + exception.getMessage());
                } finally {
                    try {
                        inBufferedReader.close();
                        outPrintStream.close();
                        dataOut.flush();
                        dataOut.close();
                        socket.close();
                        if (verbose) System.out.println("[Socket] Connection Closed Sucessfully");
                    } catch (Exception exception) {
                        System.out.println("[Socket] Error closing connection: " + exception.getMessage());
                    }
                }
            }
        });
        thread.start();
    }

    private static byte[] readFile(File file, int fileLength) throws IOException {
        byte[] fileData = new byte[fileLength];
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileData);
        } catch (FileNotFoundException exception) {
            System.out.println("[FileInputStream] ERROR: " + exception.getMessage());
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return fileData;
    }

    private static String getContentType(String fileName) {
        String contentType = "text/plain";
        System.out.println(fileName);
        if (fileName.endsWith(".html") || fileName.endsWith(".hta")) contentType = "text/html";
        return contentType;
    }
}
