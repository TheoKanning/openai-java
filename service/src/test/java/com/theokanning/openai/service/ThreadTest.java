package com.theokanning.openai.service;

import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.threads.Thread;
import com.theokanning.openai.threads.ThreadRequest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class ThreadTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createThread() {
        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .build();

        ThreadRequest threadRequest = ThreadRequest.builder()
                .messages(Collections.singletonList(messageRequest))
                .build();

        Thread thread = service.createThread(threadRequest);
        assertEquals("thread", thread.getObject());
    }
}