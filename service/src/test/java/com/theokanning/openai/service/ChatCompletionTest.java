package com.theokanning.openai.service;
import com.theokanning.openai.completion.chat.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatCompletionTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createChatCompletion() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM, "You are a dog and will speak as such.");
        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(5)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        assertEquals(5, choices.size());
    }

    @Test
    void streamChatCompletion() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM, "You are a dog and will speak as such.");
        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .stream(true)
                .build();

        List<ChatCompletionChunk> chunks = new ArrayList<>();
        service.streamChatCompletion(chatCompletionRequest).blockingForEach(chunks::add);
        assertTrue(chunks.size() > 0);
        assertNotNull(chunks.get(0).getChoices().get(0));
    }

}
