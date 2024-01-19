package com.theokanning.openai.completion.chat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Object containing a response chunk from the chat completions streaming api.
 */
@Data
public class ChatCompletionChunk {
	/**
     * Unique id assigned to this chat completion.
     */
    String id;

    /**
     * The type of object returned, should be "chat.completion.chunk"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;
    
    /**
     * The model used.
     */
    String model;

    /**
     * The fingerprint denotes the backend configuration used by the model.
     * Learn more at: https://platform.openai.com/docs/api-reference/chat/streaming#chat/streaming-system_fingerprint
     */
    @JsonProperty("system_fingerprint")
    String fingerprint;

    /**
     * A list of all generated completions.
     */
    List<ChatCompletionChoice> choices;
}