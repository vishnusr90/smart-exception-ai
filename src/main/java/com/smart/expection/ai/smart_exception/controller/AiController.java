package com.smart.expection.ai.smart_exception.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.expection.ai.smart_exception.services.AiService;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/troubleshoot")
    public String enrichException(@RequestBody ExceptionMessage exceptionMessage) {
        return aiService
                .getResolutionSteps(exceptionMessage.getMessage());
        // .forEach(doc -> System.out.println(doc));
    }

}
