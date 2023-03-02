package com.theokanning.openai.chat.completion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Given a chat conversation, the model will return a chat completion response.
 * All fields are nullable.
 * <p>
 * https://platform.openai.com/docs/api-reference/chat
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ChatCompletionRequest {


    /**
     * ID of the model to use. Currently, only and are supported.gpt-3.5-turbogpt-3.5-turbo-0301
     */
    private String model;

    /**
     * ID of the model to use. Currently, only and are supported.gpt-3.5-turbogpt-3.5-turbo-0301
     */
    private List<MessagesDTO> messages;

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    @Builder
    public static class MessagesDTO {
        private String role;
        private String content;
    }
}
