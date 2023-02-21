package com.example.springcloud_demo.feign.exception;

import feign.Request;
import feign.RetryableException;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RetryableForSaleTransactionException extends RetryableException {

    private final String methodKey;

    public RetryableForSaleTransactionException(int status, String message, Request.HttpMethod httpMethod,
                                                Throwable cause, Date retryAfter, Request request, String methodKey) {
        super(status, message, httpMethod, cause, retryAfter, request);
        this.methodKey = methodKey;
    }
}