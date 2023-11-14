package com.theokanning.openai.service;

import com.theokanning.openai.DeleteResult;
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
        System.out.println(thread.getId());
        assertEquals("thread", thread.getObject());
    }

    @Test
    void retrieveThread() {
        String threadId = "thread_K82pTg9kmhxpplGqalW6IHlc";

        Thread thread = service.retrieveThread(threadId);
        System.out.println(thread.getMetadata());
        assertEquals("thread", thread.getObject());
    }
    
    @Test
    void modifyThread() {
        String threadId = "thread_K82pTg9kmhxpplGqalW6IHlc";
        Map<String, String> metadata = new HashMap<>();
        metadata.put("action", "modify");
        ThreadRequest threadRequest = ThreadRequest.builder()
                .metadata(metadata)
                .build();
        Thread thread = service.modifyThread(threadId, threadRequest);
        assertEquals("thread", thread.getObject());
    }
    
    @Test
    void deleteThread() {
        String threadId = "thread_K82pTg9kmhxpplGqalW6IHlc";
        DeleteResult deleteResult = service.deleteThread(threadId);
        assertEquals("thread.deleted", deleteResult.getObject());
    }
}