package com.smart.expection.ai.smart_exception.services;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class AiService {

        private final ChatClient chatClient;

        private VectorStore vectorStore;

        public AiService(ChatClient.Builder builder, VectorStore vectorStore) {
                this.vectorStore = vectorStore;
                chatClient = builder
                                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                                .build();
        }

        public String getResolutionSteps(String message) {
                System.out.println("Enriching exception with message: " + message);
                Prompt prompt = new PromptTemplate(
                                """
                                                As an AI assistant, your task is to search the vector database for documents
                                                with keywords or topics as mentioned in the message which is an exception
                                                message.

                                                Here is the exception message:
                                                {message}

                                                Return the response under the following headings

                                                Summary:
                                                Steps to resolve the exception:
                                                Contact:
                                                        Team name:
                                                        Email address:
                                                """)
                                .create(Map.of("message", message));
                return chatClient.prompt()
                                .user(prompt.getContents())
                                .call()
                                .content();
        }

}
