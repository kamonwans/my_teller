package com.teller.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@RequiredArgsConstructor
public class CommonException extends Exception{
    private String errorCode;
    private String errorMessage;
    private String service;
    private HttpStatus status;

    public CommonException(final String errorCode, String errorMessage, String service, HttpStatus status) {
        super(errorCode);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.service = service;
    }
}
