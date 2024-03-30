package io.github.panghy.openai.service;

import io.github.panghy.openai.DeleteResult;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.messages.MessageRequest;
import io.github.panghy.openai.threads.Thread;
import io.github.panghy.openai.threads.ThreadRequest;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.*;

import java.util.*;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ThreadTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

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

        when(mockApi.createThread(threadRequest)).thenReturn(
            just(Thread.builder().id("thread-id").object("thread").build()));
        Thread thread = service.createThread(threadRequest);
        verify(mockApi, times(1)).createThread(threadRequest);
        threadId = thread.getId();
        assertEquals("thread", thread.getObject());
    }

    @Test
    @Order(2)
    void retrieveThread() {
        when(mockApi.retrieveThread(threadId)).thenReturn(
            just(Thread.builder().id(threadId).object("thread").build()));
        Thread thread = service.retrieveThread(threadId);
        verify(mockApi, times(1)).retrieveThread(threadId);
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
        when(mockApi.modifyThread(threadId, threadRequest)).thenReturn(
            just(Thread.builder().id(threadId).object("thread").metadata(metadata).build()));
        Thread thread = service.modifyThread(threadId, threadRequest);
        verify(mockApi, times(1)).modifyThread(threadId, threadRequest);
        assertEquals("thread", thread.getObject());
        assertEquals("modify", thread.getMetadata().get("action"));
    }
    
    @Test
    @Order(4)
    void deleteThread() {
        when(mockApi.deleteThread(threadId)).thenReturn(
            just(DeleteResult.builder().object("thread.deleted").build()));
        DeleteResult deleteResult = service.deleteThread(threadId);
        verify(mockApi, times(1)).deleteThread(threadId);
        assertEquals("thread.deleted", deleteResult.getObject());
    }
}