package br.com.luisedu.smtp.protocol;

public enum SessionState {
    /**
     * Estado inicial, logo após a conexão. Esperando por HELO/EHLO.
     */
    CONNECT,

    /**
     * Após o cliente se identificar com HELO/EHLO. Esperando por MAIL FROM.
     */
    GREETING,

    /**
     * Após o cliente enviar MAIL FROM. Esperando por RCPT TO.
     */
    MAIL,

    /**
     * Após o cliente enviar pelo menos um RCPT TO. Esperando por mais RCPT TO ou DATA.
     */
    RCPT,

    /**
     * Após o cliente enviar DATA. O servidor está recebendo o corpo do e-mail.
     */
    DATA_HEADER,

    /**
     * O servidor está recebendo o corpo do e-mail.
     */
    DATA_BODY,

    /**
     * O cliente enviou QUIT. A sessão deve ser encerrada.
     */
    QUIT
}
