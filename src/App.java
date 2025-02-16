import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import com.codingchallenges.httpserver.HandleRequest;

public class App {

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new  InetSocketAddress("127.0.0.1", 80));
        System.out.println("Server started on http://127.0.0.1:80");
        
        while(true){
            //accept the client connection
            Socket clientSocket = serverSocket.accept();
            Runnable runnable = () -> {
                try {
                    HandleRequest.handleRequest(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread clienThread = new Thread(runnable);
            clienThread.start();
            System.out.println(clienThread.getId());
            System.out.println("Client connected");
            //handle the request
        }
        //close the server
    }
}
