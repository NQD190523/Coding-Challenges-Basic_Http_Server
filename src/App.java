import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.codingchallenges.httpserver.HandleRequest;

public class App {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket();
        try {
            int port = 80; // Use 8080 instead of 80 to avoid permission issues
            serverSocket.bind(new InetSocketAddress("127.0.0.1", port));
            System.out.println("Server started on http://127.0.0.1:" + port);
        } catch (IOException e) {
            System.err.println("Error: Could not bind to port. " + e.getMessage());
            System.exit(1); // Stop execution if binding fails
        }

        // Ensure proper cleanup on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();
                System.out.println("Server shut down gracefully.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }));
        ExecutorService threadPool = Executors.newFixedThreadPool(10); // Allow 10 concurrent connections
        while(true){
            //accept the client connection
            Socket clientSocket = serverSocket.accept();
            threadPool.execute(() -> {
                try {
                    HandleRequest.handleRequest(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });            
            System.out.println("Client connected");
        }
        
        
    }
}
