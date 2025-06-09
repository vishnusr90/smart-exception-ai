package com.smart.expection.ai.smart_exception.config;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitiliazer {

    @Autowired
    private VectorStore vectorStore;

    @PostConstruct
    public void init() {
        System.out.println("Initializing data...");

        TextReader exceptionListReader = new TextReader(new ClassPathResource("/exception_list.txt"));
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(100, 100, 5,
                1000, true);
        List<Document> documents = tokenTextSplitter.split(exceptionListReader.get());
        vectorStore.add(documents);
    }
}
