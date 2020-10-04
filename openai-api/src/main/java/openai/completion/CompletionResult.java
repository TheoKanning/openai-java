package openai.completion;

import lombok.Data;

import java.util.List;

@Data
public class CompletionResult {
    String id;
    String object;
    long created;
    String model;
    List<CompletionChoice> choices;
}
