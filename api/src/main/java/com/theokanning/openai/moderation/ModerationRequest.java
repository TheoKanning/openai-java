package com.theokanning.openai.moderation;

import lombok.*;

/**
 * A request for OpenAi to detect if text violates OpenAi's content policy.
 *
 * https://beta.openai.com/docs/api-reference/moderations/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModerationRequest {

    /**
     * The input text to classify.
     */
    @NonNull
    String input;

    /**
     * The name of the model to use, defaults to text-moderation-stable.
     */
    String model;
}
