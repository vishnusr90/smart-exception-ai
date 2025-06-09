package com.smart.expection.ai.smart_exception.services;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {
        private final ChatClient chatClient;
        private VectorStore vectorStore;

        public AiService(ChatClient.Builder builder, VectorStore vectorStore) {
                // ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
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
                                                Team to contact for further assistance:
                                                        Team name:
                                                        Email address:
                                                """)
                                .create(Map.of("message", message));
                return chatClient.prompt()
                                .user(prompt.getContents())
                                .advisors(new QuestionAnswerAdvisor(vectorStore))
                                .call()
                                .content();

                // return vectorStore.similaritySearch(SearchRequest.builder()
                // .query(message)
                // .topK(5)
                // .build());

                // PromptTemplate promptTemplate = new PromptTemplate(
                // """
                // As as AI assistant, your task is to search the vector database for documents
                // with keywords or topics as mentioned in the message which is an exception
                // message

                // Here is the exception message:
                // {message}

                // Evaluation Criteria:
                // - Consider whether the document contains keywords or topics related to the
                // exception message.
                // - The evaluation should not be overly stringent; the primary objective is to
                // determine if the document can provide useful information for resolving the
                // exception.

                // Decision:
                // - If the document contains the keyword it is relevant, provide a detailed
                // step-by-step guide on how to resolve the exception.
                // - If the document is not relevant, indicate that no relevant information was
                // found.
                // """);

                // Prompt prompt = promptTemplate
                // .create(Map.of("message", message));

                // System.out.println("Prompt template :: " + prompt.getContents());
                // return chatClient
                // .prompt(prompt)
                // .call()
                // .chatResponse()
                // .getResult().getOutput().getText();
        }

}
