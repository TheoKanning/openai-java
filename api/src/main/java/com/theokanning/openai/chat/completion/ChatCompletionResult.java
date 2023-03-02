package com.theokanning.openai.chat.completion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * An object containing a response from the chat completion api
 *
 * https://platform.openai.com/docs/api-reference/chat
 */
@NoArgsConstructor
@Data
public class ChatCompletionResult {

    private String id;
    private String object;
    private Integer created;
    private List<ChoicesDTO> choices;
    private UsageDTO usage;

    @NoArgsConstructor
    @Data
    public static class UsageDTO {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
    }

    @NoArgsConstructor
    @Data
    public static class ChoicesDTO {
        private Integer index;
        private MessageDTO message;
        private String finishReason;

        @NoArgsConstructor
        @Data
        public static class MessageDTO {
            private String role;
            private String content;
        }
    }
}
