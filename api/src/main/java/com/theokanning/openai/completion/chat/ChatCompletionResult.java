package com.theokanning.openai.completion.chat;
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
     * A list of all generated completions.
     */
    List<ChatCompletionChoice> chatCompletionChoices;

    /**
     * The API usage for this request.
     */
    Usage usage;

}
