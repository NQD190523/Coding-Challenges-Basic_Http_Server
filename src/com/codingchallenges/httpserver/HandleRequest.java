package com.codingchallenges.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HandleRequest {

    private static final String WEB_ROOT = "src\\com\\codingchallenges\\www";
    public static void handleRequest(Socket clienSocket) throws IOException {
        System.out.println("Request received from " + clienSocket.getInetAddress());
        BufferedReader in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream())); 
        OutputStream out = clienSocket.getOutputStream();

        //read request
        String request = in.readLine();
        System.out.println("Request: " + request);
        if (request == null || request.isEmpty()) {
            return;
        }

        //extract the request path 
        String[] requestLine = request.split(" ");
        String path = (requestLine.length > 1) ? requestLine[1] : "/";
        System.out.println("Handling request for: " + path);

        if(path.equals("/")){
            path = "/index.html";
        }
        // Prevent directory traversal attacks (remove "..")
        path = path.replace("..", "");

        File file = new File(WEB_ROOT + path);
        System.out.println("File: " + file.getAbsolutePath());
        if(file.exists() && file.exists()){
            SendResponse.sendResponse(out, 200, "OK", file);
        } else{
            SendResponse.sendNotFound(out);
        }
        in.close();          
        out.close();
    }
}
