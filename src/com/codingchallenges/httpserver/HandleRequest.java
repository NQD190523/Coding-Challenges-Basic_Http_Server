package com.codingchallenges.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HandleRequest {
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
        String path = (requestLine.length > 1) ? requestLine[1] : "/index.html";
        System.out.println("Handling request for: " + path);

        //prepare the http response
        String response = "Requested path:" + path;
        String  responseString = "HTTP/1.1 200 OK\r\n" 
                                            + "Content-Type: text/plain\r\n" + "Content-Length:" + response.length() + "\r\n" + "\r\n" + response;
        System.out.println("Response: " + responseString);
        //send the response
        out.write(responseString.getBytes());
        out.flush();
        out.close();
        in.close();
    }
}
