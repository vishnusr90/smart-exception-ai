package com.smart.expection.ai.smart_exception.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.eclipse.angus.mail.iap.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.smart.expection.ai.smart_exception.controller.ExceptionMessage;

import jakarta.annotation.PostConstruct;

@Service
public class AiService {

        private final ChatClient chatClient;

        private VectorStore vectorStore;

        @PostConstruct
        public void init() {
                System.out.println("Initializing exception list data...");

                TextReader exceptionListReader = new TextReader(new ClassPathResource("/exception_list.txt"));
                TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
                List<Document> documents = tokenTextSplitter.split(exceptionListReader.get());
                vectorStore.add(documents);
        }

        public AiService(ChatClient.Builder builder, VectorStore vectorStore) {
                this.vectorStore = vectorStore;
                chatClient = builder
                                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                                .build();
        }

        public String getResolutionSteps(ExceptionMessage exceptionMessage) {
                System.out.println("Enriching exception with message: " + exceptionMessage.getMessage());
                Prompt prompt = new PromptTemplate(
                                """
                                                As a professional document parser, search the vector database for documents with some keywords mentioned
                                                in the exception message {message} and tag {tag}.

                                                If there are no relevant documents that matches the exception message, the just return the response as
                                                "No relevant information found for this exception." Strictly do not return another other response/information.

                                                If there are relevant information then return the response strictly under the below format. Do not return any fictional data and
                                                do not provide any context information section.

                                                Response format is as below:

                                                Hi All,

                                                An issue has occured in processing a payment.

                                                Team name: [Specify the team responsible for handling the issue]

                                                Email Address: [Provide the contact email address for further assistance]

                                                Summary: [Provide a brief summary of the issue]

                                                Mitigation Steps: [A list of mitigation steps to resolve the issue. Do not paraphrase the steps, just return the steps as it is mentioned in the document]
                                                """)
                                .create(Map.of(
                                                "message", exceptionMessage.getMessage(),
                                                "tag", exceptionMessage.getEventTag()));
                return chatClient.prompt()
                                .user(prompt.getContents())
                                .call()
                                .content();
        }

}
