package com.example.client;

public class message_model {
    String text;

    public message_model(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
