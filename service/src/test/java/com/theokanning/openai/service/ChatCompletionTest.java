package com.theokanning.openai.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.theokanning.openai.chat.ChatCompletionChoice;
import com.theokanning.openai.chat.ChatCompletionRequest;
import com.theokanning.openai.chat.Message;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class ChatCompletionTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createCompletion() {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(Collections.singletonList(new Message("user", "Hello")))
                .user("testing")
                .logitBias(new HashMap<>())
                .build();

        List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();
        assertEquals(1, choices.size());
        assertEquals("assistant", choices.get(0).getMessage().getRole());
    }

}
