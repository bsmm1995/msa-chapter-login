package com.bsmm.login.payload.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageResponse implements Serializable {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
