package com.codingchallenges.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class SendResponse {

    public static void sendResponse(OutputStream out, int statusCode, String statusMessage, File file) throws IOException { 
        byte[] fileContent ;
        try(FileInputStream fis = new FileInputStream(file)){
            fileContent = fis.readAllBytes();
        }
        // Detect content type
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) contentType = "application/octet-stream"; // Default binary type

        String  responseHeader = "HTTP/1.1 " + statusCode +" "+ statusMessage +" \r\n" 
                                            + "Content-Type: text/html\r\n" + "Content-Length:" + fileContent.length +"\r\n" 
                                            + "\r\n";
        System.out.println("Response: " + responseHeader);
        out.write(responseHeader.getBytes());
        out.write(fileContent);
        out.flush();
    }

    public static void sendNotFound(OutputStream out, int statusCode, String statusMessage, String message) throws IOException {
        String responseBody = "<html><body><h1>"+statusCode+" "+ statusMessage+"</h1><p>"+message+"</p></body></html>";
        String  responseHeader = "HTTP/1.1 " + statusCode +  " " + statusMessage + " \r\n"
                                            + "Content-Type: text/html\r\n" + "Content-Length:" + responseBody.getBytes().length 
                                            + "\r\n"
                                            + "\r\n" ;
        System.out.println("Response: " + message);
        out.write(responseHeader.getBytes());
        out.write(responseBody.getBytes());
        out.flush();
    }
}
