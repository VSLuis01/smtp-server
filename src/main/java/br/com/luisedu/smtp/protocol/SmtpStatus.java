package br.com.luisedu.smtp.protocol;

public enum SmtpStatus {
    // Positive Completion reply (2xx)
    SYSTEM_STATUS(211, "System status, or system help reply"),
    HELP_MESSAGE(214, "Help message"),
    SERVICE_READY(220, "%s Service ready"),
    CLOSING_TRANSMISSION_CHANNEL(221, "%s Service closing transmission channel"),
    OK(250, "Requested mail action okay, completed"),

    // Positive Intermediate reply (3xx)
    START_MAIL_INPUT(354, "Start mail input; end with <CRLF>.<CRLF>"),

    // Transient Negative (4xx)
    SERVICE_UNAVAILABLE(421, "%s Service not available, closing transmission channel"),

    // Permanent Negative Completion reply (5xx)
    SYNTAX_ERROR(500, "Syntax error, command unrecognized"),
    NOT_IMPLEMENTED(502, "Command not implemented"),
    BAD_SEQUENCE_OF_COMMANDS(503, "Bad sequence of commands");


    private final int code;
    private final String messageTemplate;

    SmtpStatus(int code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public int getCode() {
        return code;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public String toSmtpResponse() {
        return String.format("%d %s", this.code, this.messageTemplate.replace("%s ", ""));
    }

    public String toSmtpResponse(String domain) {
        String formatted = String.format(this.messageTemplate, domain);
        return String.format("%d %s", this.code, formatted);
    }
}