package main.java.br.com.luisedu.smtp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SmtpServer {
    private static final int PORT = 2525;
    private static final String DOMAIN = "smtp.luisedu.com.br";

    public static void main(String[] args) {
        System.out.println("Starting SMTP server...");
        System.out.println("Domain: " + DOMAIN);
        System.out.println("Port: " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening on port " + PORT);


            while (true) {
                System.out.println("Waiting for connection...");

                Socket clientSocket = serverSocket.accept();

                System.out.println("Accepted connection from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
