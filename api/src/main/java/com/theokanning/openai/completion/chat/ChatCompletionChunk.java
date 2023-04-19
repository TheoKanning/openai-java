package com.theokanning.openai.completion.chat;
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
     * A list of all generated completions.
     */
    List<ChatCompletionChoice> choices;
}