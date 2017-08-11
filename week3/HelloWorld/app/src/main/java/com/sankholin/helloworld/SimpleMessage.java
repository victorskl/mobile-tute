package com.sankholin.helloworld;

import java.io.Serializable;

public class SimpleMessage implements Serializable {
    private String message;

    public SimpleMessage() {
    }

    public SimpleMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
