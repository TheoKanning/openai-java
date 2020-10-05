package openai.completion;

import lombok.Data;

import java.util.List;

/**
 * An object containing a response from the completion api
 *
 * https://beta.openai.com/docs/api-reference/create-completion
 */
@Data
public class CompletionResult {
    /**
     * A unique id assigned to this completion
     */
    String id;

    /**
     * The type of object returned, should be "text_completion"
     */
    String object;

    /**
     * The creation time in epoch milliseconds.
     */
    long created;

    /**
     * The GPT-3 model used
     */
    String model;

    /**
     * A list of generated completions
     */
    List<CompletionChoice> choices;
}
