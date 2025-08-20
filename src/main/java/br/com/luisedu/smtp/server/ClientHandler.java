package main.java.br.com.luisedu.smtp.server;

import main.java.br.com.luisedu.smtp.protocol.SmtpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
            // 1. Enviar a mensagem de boas-vindas (220) assim que o cliente se conecta.
            writer.println(SmtpStatus.SERVICE_READY.toSmtpResponse(serverDomain));

            System.out.println("SERVER: " + SmtpStatus.SERVICE_READY.toSmtpResponse(serverDomain));

            String clientMsg;

            // 2. Loop para ler as mensagens do cliente.
            while ((clientMsg = reader.readLine()) != null) {
                System.out.println("CLIENT: " + clientMsg);


                writer.println(SmtpStatus.OK.toSmtpResponse());

                // TODO: lógica para processar os comandos (HELO, EHLO, MAIL FROM, etc.)
            }

        } catch (IOException e) {
            System.err.println("Error opening client socket: " + e.getMessage());
        } finally {
            try {
                this.clientSocket.close();

                System.out.println("Closing connection with " + clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
