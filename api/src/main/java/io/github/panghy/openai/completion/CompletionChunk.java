package io.github.panghy.openai.completion;

import lombok.Data;
import java.util.List;

/**
 * Object containing a response chunk from the completions streaming api.
 *
 * https://beta.openai.com/docs/api-reference/completions/create
 */
@Data
public class CompletionChunk {
    /**
     * A unique id assigned to this completion.
     */
    String id;

    /**https://beta.openai.com/docs/api-reference/create-completion
     * The type of object returned, should be "text_completion"
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
     * A list of generated completions.
     */
    List<CompletionChoice> choices;
}
