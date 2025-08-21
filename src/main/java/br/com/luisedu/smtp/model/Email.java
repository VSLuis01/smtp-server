package br.com.luisedu.smtp.model;

import java.util.ArrayList;
import java.util.List;

public class Email {
    private String from;
    private final List<String> to = new ArrayList<>();
    private final StringBuilder data = new StringBuilder();

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void addTo(String recipient) {
        this.to.add(recipient);
    }

    public StringBuilder getData() {
        return data;
    }

    public void appendData(String data) {
        this.data.append(data).append("\n");
    }

    @Override
    public String toString() {
        return "Email{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", data='" + data.substring(0, Math.min(data.length(), 50)) + "..." + '\'' +
                '}';
    }
}
