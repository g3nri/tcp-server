package tcpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {

    private static final int port = 9090;

    public static void main(String[] args) {
        // ExecutorService represents a thread pool
        ExecutorService pool = Executors.newFixedThreadPool(10);

        // ServerSocket opens a server-side TCP socket
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            // Infinite loop
            while (true) {
                // accept() is a blocking call and waits until a client connects and returns.
                //System.out.println("Before accept");
                Socket client = serverSocket.accept();
                //System.out.println("After accept");
                pool.submit(new ClientHandler(client));
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
