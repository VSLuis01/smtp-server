package br.com.luisedu.smtp.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SmtpServer {
    private static final Logger logger = LoggerFactory.getLogger(SmtpServer.class);

    private static final int PORT = 2525;
    private static final String DOMAIN = "smtp.luisedu.com.br";

    public static void main(String[] args) {
        logger.info("Starting SMTP server on port {}", PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Listening on port {}", PORT);


            while (true) {
                logger.debug("Listening on port {}", PORT);

                Socket clientSocket = serverSocket.accept();

                logger.info("Client connected from {}:{}", clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort());

                ClientHandler handler = new ClientHandler(clientSocket, DOMAIN);

                Thread clientThread = new Thread(handler);

                clientThread.start();
            }
        } catch (IOException e) {
            logger.error("Fatal error on server: {}", e.getMessage(), e);
        }
    }
}
