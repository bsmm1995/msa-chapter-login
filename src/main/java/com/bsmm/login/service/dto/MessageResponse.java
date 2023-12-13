package com.bsmm.login.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageResponse implements Serializable {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
