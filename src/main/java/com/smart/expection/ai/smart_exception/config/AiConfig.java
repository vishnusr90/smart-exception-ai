package com.smart.expection.ai.smart_exception.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient.Builder chatClientBuilder(@Autowired ChatModel chatModel) {
        return ChatClient.builder(chatModel);
    }
}
