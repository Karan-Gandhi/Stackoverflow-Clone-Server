package com.karangandhi.stackoverflow.clone.Routes;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Routes implements Runnable {
    static final File WEB_ROOT = new File("./web/views");
    static final String DEFAULT_FILE = "index.html";

    private Socket socket;
    private boolean verbose;
    private String route = null;
    private BufferedReader inBufferedReader = null;
    private PrintStream outPrintStream = null;
    private BufferedOutputStream dataOut = null;

    public Routes(Socket socket, boolean verbose) {
        this.socket = socket;
        this.verbose = verbose;
    }

    private static byte[] get200Headder(String contentType, int fileLength) {
        String header = "HTTP/1.1 200 OK" +
                "\nServer: Java HTTP Server from Karan Gandhi : 1.0" +
                "\nDate: " + new Date() +
                "\nContent-type: " + contentType +
                "\nContent-length: " + fileLength +
                "\n";
        return header.getBytes();
    }

    @Override
    public void run() {
        try {
            inBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new BufferedOutputStream(socket.getOutputStream());
            String line = inBufferedReader.readLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, " ");
            String method = stringTokenizer.nextToken().toUpperCase();
            route = stringTokenizer.nextToken().toLowerCase();
            if (verbose) System.out.println("Method: " + method + "; File: " + route);

            // Methods
            if (method.equals("GET")) this.GET(method, route);
            else if (method.equals("POST")) this.POST(method, route);
            else if (method.equals("PUT")) this.PUT(method, route);
            else if (method.equals("DELETE")) this.DELETE(method, route);

        } catch(FileNotFoundException exception) {
            System.out.println("[Socket] ERROR(FileNotFound): " + exception.getMessage());
        } catch (IOException exception) {
            System.out.println("[Socket] ERROR: " + exception.getMessage());
        } finally {
            try {
                inBufferedReader.close();
                outPrintStream.close();
                dataOut.close();
                socket.close();
            } catch (Exception exception) {
                System.out.println("[Socket] Error closing connection: " + exception.getMessage());
            }
        }
    }

    private void GET(String method, String route) throws IOException {
//        if (route.endsWith("/")) sendFile(dataOut, DEFAULT_FILE);
//        int fileLength = (int) file.length();
        String content = "text/plain";
        byte[] fileData = "Hello World".getBytes();

        byte[] header = get200Headder(content, fileData.length);
        dataOut.write(header, 0, header.length);
        dataOut.write(fileData, 0, fileData.length);

        dataOut.flush();
        dataOut.close();
    }

    private void POST(String method, String route) {}

    private void PUT(String method, String route) {}

    private void DELETE(String method, String route) {}

    private static void sendFile(BufferedOutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, DEFAULT_FILE);
        int fileLength = (int) file.length();
        String content = getContentType(fileRequested);
        byte[] fileData = readFile(file, fileLength);

        byte[] header = get200Headder(content, fileLength);
        dataOut.write(header, 0, header.length);
        dataOut.write(fileData, 0, fileLength);

        dataOut.flush();
        dataOut.close();
    }

    private static void sendFile(BufferedOutputStream dataOut, String fileRequested, HashMap<String, String> dataToReplace) throws IOException {
        File file = new File(WEB_ROOT, DEFAULT_FILE);
        int fileLength = (int) file.length();
        String content = getContentType(fileRequested);
        byte[] fileData = readFile(file, fileLength);

        byte[] header = get200Headder(content, fileLength);
        dataOut.write(header, 0, header.length);

        String fileString = new String(fileData);
        dataToReplace.forEach((key, data) -> {
            fileString.replace(key, data);
        });
        fileData = fileString.getBytes();
        dataOut.write(fileData, 0, fileLength);

        dataOut.flush();
        dataOut.close();
    }

    private static byte[] readFile(File file, int fileLength) throws IOException {
        byte[] fileData = new byte[fileLength];
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileData);
        } finally {
            if (fileInputStream != null) fileInputStream.close();
        }

        return fileData;
    }

    private static String getContentType(String fileName) {
        String contentType = "text/plain";
        if (fileName.endsWith(".html") || fileName.endsWith(".hta")) contentType = "text/html";
        return contentType;
    }
}
