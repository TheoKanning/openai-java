package com.theokanning.openai.embedding;

import lombok.*;

import java.util.List;

/**
 * Creates an embedding vector representing the input text.
 *
 * https://beta.openai.com/docs/api-reference/embeddings/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmbeddingRequest {

    /**
     * The name of the model to use.
     * Required if using the new v1/embeddings endpoint.
     */
    String model;

    /**
     * Input text to get embeddings for, encoded as a string or array of tokens.
     * To get embeddings for multiple inputs in a single request, pass an array of strings or array of token arrays.
     * Each input must not exceed 2048 tokens in length.
     * <p>
     * Unless you are embedding code, we suggest replacing newlines (\n) in your input with a single space,
     * as we have observed inferior results when newlines are present.
     */
    @NonNull
    List<String> input;

    /**
     * A unique identifier representing your end-user, which will help OpenAI to monitor and detect abuse.
     */
    String user;

    /**
     * The number of dimensions to be used by the model to represent each input text as the feature
     * vector.
     */
    @Setter(AccessLevel.NONE)
    Integer dimensions = null;

    /**
     * Set the number of dimensions to be used by the model to represent each input text as the feature vector.
     *
     * @param dimensions The number of dimensions to be used by the model. Only supported for models from or after
     *                   third generation.
     */
    public void setDimensions(Integer dimensions) {
        EmbeddingModel embeddingModel = EmbeddingModel.fromValue(this.model);

        if (embeddingModel.getGeneration() > 2) {
            this.dimensions = dimensions;
        } else {
            this.dimensions = null;
        }
    }
}
