package com.theokanning.openai.service;

import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class EmbeddingTest {

    String token = System.getenv("OPENAI_TOKEN");
    com.theokanning.openai.service.OpenAiService service = new OpenAiService(token);

    @Test
    void createEmbeddings() {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")
                .input(Collections.singletonList("The food was delicious and the waiter..."))
                .build();

        List<Embedding> embeddings = service.createEmbeddings(embeddingRequest).getData();

        assertFalse(embeddings.isEmpty());
        assertFalse(embeddings.get(0).getEmbedding().isEmpty());
    }
}
