package tcpserver;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {

    // Timeout for waiting client input
    private static final int TIMEOUT_MS = 30_000;
    // Socket represents the TCP connection with the client
    private final Socket socket;
    // tcpserver.CommandProcessor handles the business logic of commands
    private final CommandProcessor processor = new CommandProcessor();

    // Constructor receives the client socket from TcpServer
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

//        Get name of thread.
//        System.out.println(
//                "Handled by thread: " + Thread.currentThread().getName()
//        );

        // try-with-resources
        try (Socket client = socket;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             // PrintWriter sends text data to the client
             // autoFlush = true forces immediate data sending
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true)
        ) {
            client.setSoTimeout(TIMEOUT_MS);

            out.println("Commands: TIME, UPPER <text>, REVERSE <text>, QUIT");

            String line;
            while ((line = in.readLine()) != null) {

                System.out.println("Client sends: " + line);

                // Process the command
                String response = processor.process(line);

                if (response.equals("QUIT")) {
                    System.out.println("Server responds: Goodbye");
                    out.println("Goodbye");
                    break;
                }

                System.out.println("Server responds: " + response);
                out.println(response);
            }
            System.out.println("[connection closes]");

        } catch (SocketTimeoutException e) {
            System.out.println("Client timeout");
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
