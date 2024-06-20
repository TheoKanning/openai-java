package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Data;

/**
 * see {@link ChatCompletionRequest} documentation.
 */
@Data
@Builder
public class ChatResponseFormat {
    private ResponseFormat type;

    public enum ResponseFormat {
        TEXT("text"),
        JSON("json_object");

        private final String value;

        ResponseFormat(final String value) {
            this.value = value;
        }

        @JsonValue
        public String value() {
            return value;
        }
    }
}