package openai.completion;

import lombok.Data;

import java.util.List;

@Data
public class CompletionRequest {
    String prompt;
    Integer maxTokens;
    Double temperature;
    Double topP;
    Integer n;
    Boolean stream;
    Integer logprobs;
    Boolean echo;
    List<String> stop; //todo test this
    Double presencePenalty;
    Double frequencyPenalty;
    Integer bestOf;
}
