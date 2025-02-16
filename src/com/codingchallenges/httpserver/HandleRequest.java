package com.codingchallenges.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HandleRequest {

    private static final String WEB_ROOT ;
    static {
        try {
            WEB_ROOT = new File("src/com/codingchallenges/www").getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException("Error initializing web root", e);
        }
    }
    public static void handleRequest(Socket clienSocket) throws IOException {

        System.out.println("Request received from " + clienSocket.getInetAddress());
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream())); 
            OutputStream out = clienSocket.getOutputStream();

            //read request
            String request = in.readLine();
            System.out.println("Request: " + request);
            if (request == null || request.isEmpty()) {
                SendResponse.sendNotFound(out, 400, "Bad Request", "Invalid Request!");
                return;
            }
            //extract the request path 
            String[] requestLine = request.split(" ");
            String path = (requestLine.length > 1) ? requestLine[1] : "/";
            System.out.println("Handling request for: " + path);

            if(path.equals("/")){
                path = "/index.html";
            }

            //Resolve absolute file path safely
            File file = new File(WEB_ROOT + path).getCanonicalFile();
            System.out.println("File: " + file.getAbsolutePath());

            //Check if file exists and is not a directory
            if (!file.exists() || file.isDirectory()) {
                SendResponse.sendNotFound(out, 404, "Not Found", "The requested resource was not found.");
                return;
            }
            //check if the file is within the web root
            if(!file.getAbsolutePath().startsWith(new File(WEB_ROOT).getCanonicalPath())) {
                SendResponse.sendNotFound(out, 403, "Forbidden", "Access Denied!");
                return;
            }
            //send the response
            SendResponse.sendResponse(out, 200, "OK", file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clienSocket.close();
            System.out.println("Client disconnected");
        }
    }
}
