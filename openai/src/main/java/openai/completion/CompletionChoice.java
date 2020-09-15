package openai.completion;

import lombok.Data;

@Data
public class CompletionChoice {
    String text;
    Integer index;
    // todo add logprobs
    String finish_reason;
}
