package com.sap.bulletinboard.ads.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// need to define exceptions with response status, not predefined
@ResponseStatus(code=HttpStatus.NOT_FOUND, reason="lindasy not found id")
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
