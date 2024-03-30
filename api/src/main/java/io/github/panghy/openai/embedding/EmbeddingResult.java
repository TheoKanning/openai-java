package io.github.panghy.openai.embedding;

import io.github.panghy.openai.Usage;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * An object containing a response from the answer api
 *
 * https://beta.openai.com/docs/api-reference/embeddings/create
 */
@Data
@Jacksonized
@Builder
public class EmbeddingResult {

    /**
     * The GPTmodel used for generating embeddings
     */
    String model;

    /**
     * The type of object returned, should be "list"
     */
    String object;

    /**
     * A list of the calculated embeddings
     */
    List<Embedding> data;

    /**
     * The API usage for this request
     */
    Usage usage;
}
