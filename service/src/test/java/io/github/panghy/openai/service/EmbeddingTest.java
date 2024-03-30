package io.github.panghy.openai.service;

import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.embedding.Embedding;
import io.github.panghy.openai.embedding.EmbeddingRequest;
import io.github.panghy.openai.embedding.EmbeddingResult;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class EmbeddingTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void createEmbeddings() {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")
                .input(Collections.singletonList("The food was delicious and the waiter..."))
                .build();

        when(mockApi.createEmbeddings(embeddingRequest)).thenReturn(just(
            EmbeddingResult.builder().data(
                    List.of(Embedding.builder().embedding(List.of(0.1, 0.2, 0.3)).build()))
                .build()));
        List<Embedding> embeddings = service.createEmbeddings(embeddingRequest).getData();

        assertFalse(embeddings.isEmpty());
        assertFalse(embeddings.get(0).getEmbedding().isEmpty());
    }
}
