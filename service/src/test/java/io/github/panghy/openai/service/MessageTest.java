package io.github.panghy.openai.service;

import io.github.panghy.openai.ListSearchParameters;
import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.file.File;
import io.github.panghy.openai.messages.Message;
import io.github.panghy.openai.messages.MessageFile;
import io.github.panghy.openai.messages.MessageRequest;
import io.github.panghy.openai.messages.ModifyMessageRequest;
import io.github.panghy.openai.threads.Thread;
import io.github.panghy.openai.threads.ThreadRequest;
import io.github.panghy.openai.service.OpenAiService;
import io.reactivex.Single;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class MessageTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    static String threadId;

    @Test
    void createMessage() {
        when(mockApi.uploadFile(any(), any())).thenReturn(just(File.builder().
            id("file-id").
            build()));
        File file = service.uploadFile("assistants", "src/test/resources/penguin.png");
        verify(mockApi, times(1)).uploadFile(any(), any());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .fileIds(Collections.singletonList(file.getId()))
                .metadata(metadata)
                .build();

        when(mockApi.createMessage(threadId, messageRequest)).thenReturn(just(Message.builder()
            .id("message-id")
            .object("thread.message")
            .fileIds(Collections.singletonList(file.getId()))
            .build()));
        Message message = service.createMessage(threadId, messageRequest);
        verify(mockApi, times(1)).createMessage(threadId, messageRequest);

        assertNotNull(message.getId());
        assertEquals("thread.message", message.getObject());
        assertEquals(1, message.getFileIds().size());
    }

    @Test
    void retrieveMessage() {
        String messageId = "test-message-id";

        when(mockApi.retrieveMessage(threadId, messageId)).thenReturn(just(Message.builder()
            .id(messageId)
            .build()));
        Message message = service.retrieveMessage(threadId, messageId);
        verify(mockApi, times(1)).retrieveMessage(threadId, messageId);

        assertEquals(messageId, message.getId());
    }

    @Test
    void modifyMessage() {
        String messageId = "test-message-id";

        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        ModifyMessageRequest request = ModifyMessageRequest.builder()
                .metadata(metadata)
                .build();
        when(mockApi.modifyMessage(threadId, messageId, request)).thenReturn(just(Message.builder()
            .id(messageId)
            .metadata(metadata)
            .build()));
        Message message = service.modifyMessage(threadId, messageId, request);
        verify(mockApi, times(1)).modifyMessage(threadId, messageId, request);

        assertEquals(messageId, message.getId());
        assertEquals("value", message.getMetadata().get("key"));
    }

    @Test
    void listMessages() {
        ThreadRequest threadRequest = ThreadRequest.builder()
                .build();
        when(mockApi.createThread(threadRequest)).thenReturn(just(Thread.builder()
            .id("separate-thread-id")
            .build()));
        String separateThreadId = service.createThread(threadRequest).getId();
        verify(mockApi, times(1)).createThread(threadRequest);

        when(mockApi.listMessages(separateThreadId))
            .thenReturn(just(OpenAiResponse.<Message>builder().data(List.of(
                Message.builder().id("message1").build(),
                Message.builder().id("message2").build(),
                Message.builder().id("message3").build()
            )).build()));
        List<Message> messages = service.listMessages(separateThreadId).getData();
        verify(mockApi, times(1)).listMessages(separateThreadId);

        assertEquals(3, messages.size());
    }

    @Test
    void retrieveAndListMessageFile() {
        when(mockApi.uploadFile(any(), any())).thenReturn(just(File.builder().
            id("file-id").
            build()));
        File file = service.uploadFile("assistants", "src/test/resources/penguin.png");
        verify(mockApi, times(1)).uploadFile(any(), any());

        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .fileIds(Collections.singletonList(file.getId()))
                .build();

        when(mockApi.createMessage(threadId, messageRequest)).thenReturn(just(Message.builder()
            .id("message-id")
            .build()));
        Message message = service.createMessage(threadId, messageRequest);
        verify(mockApi, times(1)).createMessage(threadId, messageRequest);

        when(mockApi.retrieveMessageFile(threadId, message.getId(), file.getId()))
            .thenReturn(just(MessageFile.builder()
                .id(file.getId())
                .messageId(message.getId())
                .build()));
        MessageFile messageFile = service.retrieveMessageFile(threadId, message.getId(), file.getId());
        verify(mockApi, times(1)).retrieveMessageFile(threadId, message.getId(), file.getId());

        assertEquals(file.getId(), messageFile.getId());
        assertEquals(message.getId(), messageFile.getMessageId());

        when(mockApi.listMessageFiles(threadId, message.getId(), Map.of()))
            .thenReturn(just(OpenAiResponse.<MessageFile>builder().data(List.of(
                MessageFile.builder().id(file.getId()).messageId(message.getId()).build()
            )).build()));
        List<MessageFile> messageFiles = service.listMessageFiles(threadId, message.getId(),
            new ListSearchParameters()).getData();
        assertEquals(1, messageFiles.size());
    }
}