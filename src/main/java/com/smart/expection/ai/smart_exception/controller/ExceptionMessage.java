package com.smart.expection.ai.smart_exception.controller;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ExceptionMessage {
    String clientId;
    String refNumber;
    String amount;
    String currency;
    @NonNull
    private String message;
    private String eventTag;
}
