package com.theokanning.openai.completion.chat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.Usage;
import lombok.Data;

import java.util.List;

/**
 * Object containing a response from the chat completions api.
 */
@Data
public class ChatCompletionResult {

    /**
     * Unique id assigned to this chat completion.
     */
    String id;

    /**
     * The type of object returned, should be "chat.completion"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;
    
    /**
     * The GPT model used.
     */
    String model;

    /**
     * The fingerprint denotes the backend configuration used by the model.
     * Learn more at: https://platform.openai.com/docs/api-reference/chat/object#chat/object-system_fingerprint
     */
    @JsonProperty("system_fingerprint")
    String fingerprint;

    /**
     * A list of all generated completions.
     */
    List<ChatCompletionChoice> choices;

    /**
     * The API usage for this request.
     */
    Usage usage;

}
