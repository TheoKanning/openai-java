package com.theokanning.openai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the error body when an OpenAI request fails
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiError {

    public OpenAiErrorDetails error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpenAiErrorDetails {
        /**
         * Human-readable error message
         */
        String message;

        /**
         * OpenAI error type, for example "invalid_request_error"
         * https://platform.openai.com/docs/guides/error-codes/python-library-error-types
         */
        String type;

        String param;

        /**
         * OpenAI error code, for example "invalid_api_key"
         */
        String code;
    }
}
