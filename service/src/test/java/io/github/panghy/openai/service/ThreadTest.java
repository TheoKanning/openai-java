package io.github.panghy.openai.service;

import io.github.panghy.openai.DeleteResult;
import io.github.panghy.openai.messages.MessageRequest;
import io.github.panghy.openai.threads.Thread;
import io.github.panghy.openai.threads.ThreadRequest;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ThreadTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    static String threadId;

    @Test
    @Order(1)
    void createThread() {
        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .build();

        ThreadRequest threadRequest = ThreadRequest.builder()
                .messages(Collections.singletonList(messageRequest))
                .build();

        Thread thread = service.createThread(threadRequest);
        threadId = thread.getId();
        assertEquals("thread", thread.getObject());
    }

    @Test
    @Order(2)
    void retrieveThread() {
        Thread thread = service.retrieveThread(threadId);
        System.out.println(thread.getMetadata());
        assertEquals("thread", thread.getObject());
    }
    
    @Test
    @Order(3)
    void modifyThread() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("action", "modify");
        ThreadRequest threadRequest = ThreadRequest.builder()
                .metadata(metadata)
                .build();
        Thread thread = service.modifyThread(threadId, threadRequest);
        assertEquals("thread", thread.getObject());
        assertEquals("modify", thread.getMetadata().get("action"));
    }
    
    @Test
    @Order(4)
    void deleteThread() {
        DeleteResult deleteResult = service.deleteThread(threadId);
        assertEquals("thread.deleted", deleteResult.getObject());
    }
}