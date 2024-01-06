package com.theokanning.openai.completion.chat;

/**
 * see {@link ChatCompletionRequest} documentation.
 */
public enum ChatResponseFormat {
    TEXT("text"),
    JSON("json");

    private final String value;

    ChatResponseFormat(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
