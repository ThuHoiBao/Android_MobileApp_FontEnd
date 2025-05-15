package com.example.retofit2.dto.responseDTO;

public class Message {
    private String text;
    private boolean isSent;
    private String time;

    public Message(String text, boolean isSent, String time) {
        this.text = text;
        this.isSent = isSent;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
