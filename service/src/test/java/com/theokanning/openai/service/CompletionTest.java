package com.theokanning.openai.service;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CompletionTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createCompletion() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("ada")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .n(5)
                .maxTokens(50)
                .user("testing")
                .logitBias(new HashMap<>())
                .logprobs(5)
                .build();

        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        assertEquals(5, choices.size());
        assertNotNull(choices.get(0).getLogprobs());
    }
}
