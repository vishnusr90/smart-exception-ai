package com.smart.expection.ai.smart_exception.controller;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.expection.ai.smart_exception.services.AiService;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@RestController
@RequestMapping("/ai")
public class AiController {

    @NonNull
    private AiService aiService;

    @PostMapping("/enrich")
    public ChatResponse enrichException(@RequestBody ExceptionMessage exceptionMessage) {
        System.out.println(exceptionMessage.getMessage());
        return aiService.enrichException(exceptionMessage.getMessage());
    }

}
