package br.com.luisedu.smtp.server;

import br.com.luisedu.smtp.protocol.SessionState;
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

    // Variáveis da sessão do cliente
    private SessionState currentState;
    private boolean isRunning;

    public ClientHandler(Socket clientSocket, String serverDomain) {
        this.clientSocket = clientSocket;
        this.serverDomain = serverDomain;

        // O estado inicial da sessão é CONNECT.
        this.currentState = SessionState.CONNECT;
        this.isRunning = true;
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
            while (isRunning && (clientMsg = reader.readLine()) != null) {
                logger.info("<- [{}]: {}", clientIp, clientMsg);

                processCommand(writer, clientMsg);
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

    private void processCommand(PrintWriter writer, String command) {
        String upperCaseCommand = command.toUpperCase();

        if (upperCaseCommand.equals("QUIT")) {
            sendResponse(writer, SmtpStatus.CLOSING_TRANSMISSION_CHANNEL, serverDomain);
            this.currentState = SessionState.QUIT;
            this.isRunning = false;
            return;
        }

        switch (currentState) {
            case CONNECT:
                if (upperCaseCommand.startsWith("HELO") || upperCaseCommand.startsWith("EHLO")) {
                    sendResponse(writer, SmtpStatus.OK);
                    // TRANSIÇÃO DE ESTADO: Agora esperando por MAIL FROM.
                    this.currentState = SessionState.GREETING;
                } else {
                    sendResponse(writer, SmtpStatus.BAD_SEQUENCE_OF_COMMANDS);
                }
                break;

            case GREETING:
                // TODO: lógica para o comando MAIL FROM.
                logger.warn("Comando não implementado para o estado GREETING: {}", command);
                sendResponse(writer, SmtpStatus.NOT_IMPLEMENTED);
                break;

            // Outros casos (MAIL, RCPT, etc.) serão adicionados depois.
            default:
                logger.warn("Comando recebido em um estado não tratado ({}): {}", currentState, command);
                sendResponse(writer, SmtpStatus.SYNTAX_ERROR);
                break;
        }
    }

    private void sendResponse(PrintWriter writer, SmtpStatus status) {
        String response = status.toSmtpResponse();
        writer.println(response);
        logger.info("-> [{}]: {}", clientSocket.getInetAddress().getHostAddress(), response);
    }

    private void sendResponse(PrintWriter writer, SmtpStatus status, String domain) {
        String response = status.toSmtpResponse(domain);
        writer.println(response);
        logger.info("-> [{}]: {}", clientSocket.getInetAddress().getHostAddress(), response);
    }
}
