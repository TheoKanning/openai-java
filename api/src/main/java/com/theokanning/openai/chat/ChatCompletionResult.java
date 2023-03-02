package com.theokanning.openai.chat;

import com.theokanning.openai.Usage;
import lombok.Data;

import java.util.List;

/**
 * An object containing a response from the completion api
 *
 * https://beta.openai.com/docs/api-reference/completions/create
 */
@Data
public class ChatCompletionResult {
    /**
     * A unique id assigned to this completion.
     */
    String id;

    /**
     * The type of object returned, should be "text_completion"
     */
    String object;

    /**
     * The creation time in epoch seconds.
     */
    long created;

    /**
     * A list of generated completions.
     */
    List<ChatCompletionChoice> choices;

    /**
     * The API usage for this request
     */
    Usage usage;
}
