package br.com.luisedu.smtp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.luisedu.smtp.protocol.SmtpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    private final Socket clientSocket;
    private final String serverDomain;

    public ClientHandler(Socket clientSocket, String serverDomain) {
        this.clientSocket = clientSocket;
        this.serverDomain = serverDomain;
    }

    @Override
    public void run() {

        try (
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String clientIp = clientSocket.getInetAddress().getHostAddress();
            String welcomeMessage = SmtpStatus.SERVICE_READY.toSmtpResponse(serverDomain);

            // 1. Enviar a mensagem de boas-vindas (220) assim que o cliente se conecta.
            writer.println(welcomeMessage);

            logger.info("-> [{}]: {}", clientIp, welcomeMessage);

            String clientMsg;

            // 2. Loop para ler as mensagens do cliente.
            while ((clientMsg = reader.readLine()) != null) {
                logger.info("<- [{}]: {}", clientIp, clientMsg);

                writer.println(SmtpStatus.OK.toSmtpResponse());

                // TODO: lógica para processar os comandos (HELO, EHLO, MAIL FROM, etc.)
            }

        } catch (IOException e) {
            logger.error("Communication error with client: {}", e.getMessage());
        } finally {
            try {
                this.clientSocket.close();

                logger.info("Closing connection with {}", clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                logger.error("Error closing client socket.", e);
            }
        }

    }
}
