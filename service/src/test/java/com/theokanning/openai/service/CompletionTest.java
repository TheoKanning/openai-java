package com.theokanning.openai.service;

import com.theokanning.openai.chat.completion.ChatCompletionRequest;
import com.theokanning.openai.chat.completion.ChatCompletionResult;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


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
                .build();

        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        assertEquals(5, choices.size());
    }

    @Test
    void createChatCompletion() {
        List<ChatCompletionRequest.MessagesDTO> messages= new ArrayList<>();
        ChatCompletionRequest.MessagesDTO messagesDTO = ChatCompletionRequest.MessagesDTO.builder()
                .role("user")
                .content("Hello!")
                .build();
        messages.add(messagesDTO);

        final ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .build();

        final List<ChatCompletionResult.ChoicesDTO> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        assertFalse(choices.isEmpty());
    }
}
