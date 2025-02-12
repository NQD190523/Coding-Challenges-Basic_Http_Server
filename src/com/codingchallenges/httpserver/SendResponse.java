package com.codingchallenges.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SendResponse {

    public static void sendResponse(OutputStream out, int statusCode, String statusMessage, File file) throws IOException { 
        byte[] fileContent = new FileInputStream(file).readAllBytes();
        String  responseString = "HTTP/1.1 " + statusCode +" "+ statusMessage +" \r\n" 
                                            + "Content-Type: text/html\r\n" + "Content-Length:" + fileContent.length + "\r\n" 
                                            + "\r\n";
        System.out.println("Response: " + responseString);
        //prepare the http response
        //send the response
        out.write(responseString.getBytes());
        out.write(fileContent);
        out.flush();
    }

    public static void sendNotFound(OutputStream out) throws IOException {
        String responseBody = "<html><body><h1>404 Not Found</h1><p>The requested resource was not found.</p></body></html>";
        String  responseString = "HTTP/1.1 404 Not Found  \r\n"
                                            + "Content-Type: text/html\r\n" + "Content-Length:" + responseBody.length() + "\r\n"
                                            + "\r\n" + responseBody;
        System.out.println("Response: " + responseString);
        //prepare the http response
        //send the response
        out.write(responseString.getBytes());
        out.flush();
    }
}
