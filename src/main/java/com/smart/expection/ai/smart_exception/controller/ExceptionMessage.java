package com.smart.expection.ai.smart_exception.controller;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ExceptionMessage {
    @NonNull
    private String message;
    private String eventTag;
}
