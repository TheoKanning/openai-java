package io.github.panghy.openai.service;

import io.github.panghy.openai.ListSearchParameters;
import io.github.panghy.openai.file.File;
import io.github.panghy.openai.messages.Message;
import io.github.panghy.openai.messages.MessageFile;
import io.github.panghy.openai.messages.MessageRequest;
import io.github.panghy.openai.messages.ModifyMessageRequest;
import io.github.panghy.openai.threads.ThreadRequest;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MessageTest {

    static OpenAiService service;

    static String threadId;

    @BeforeAll
    static void setup() {
        String token = System.getenv("OPENAI_TOKEN");
        service = new OpenAiService(token);

        ThreadRequest threadRequest = ThreadRequest.builder()
                .build();
        threadId = service.createThread(threadRequest).getId();
    }

    @AfterAll
    static void teardown() {
        try {
            service.deleteThread(threadId);
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    void createMessage() {
        File file = service.uploadFile("assistants", "src/test/resources/penguin.png");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .fileIds(Collections.singletonList(file.getId()))
                .metadata(metadata)
                .build();

        Message message = service.createMessage(threadId, messageRequest);

        assertNotNull(message.getId());
        assertEquals("thread.message", message.getObject());
        assertEquals(1, message.getFileIds().size());
    }

    @Test
    void retrieveMessage() {
        String messageId = createTestMessage().getId();

        Message message = service.retrieveMessage(threadId, messageId);

        assertEquals(messageId, message.getId());
    }

    @Test
    void modifyMessage() {
        String messageId = createTestMessage().getId();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", "value");

        ModifyMessageRequest request = ModifyMessageRequest.builder()
                .metadata(metadata)
                .build();
        Message message = service.modifyMessage(threadId, messageId, request);

        assertEquals(messageId, message.getId());
        assertEquals("value", message.getMetadata().get("key"));
    }

    @Test
    void listMessages() {
        ThreadRequest threadRequest = ThreadRequest.builder()
                .build();
        String separateThreadId = service.createThread(threadRequest).getId();
        createTestMessage(separateThreadId);
        createTestMessage(separateThreadId);
        createTestMessage(separateThreadId);

        List<Message> messages = service.listMessages(separateThreadId).getData();

        assertEquals(3, messages.size());
    }

    @Test
    void retrieveAndListMessageFile() {
        File file = service.uploadFile("assistants", "src/test/resources/penguin.png");
        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .fileIds(Collections.singletonList(file.getId()))
                .build();

        Message message = service.createMessage(threadId, messageRequest);

        MessageFile messageFile = service.retrieveMessageFile(threadId, message.getId(), file.getId());

        assertEquals(file.getId(), messageFile.getId());
        assertEquals(message.getId(), messageFile.getMessageId());

        List<MessageFile> messageFiles = service.listMessageFiles(threadId, message.getId(), new ListSearchParameters()).getData();
        assertEquals(1, messageFiles.size());
    }

    Message createTestMessage() {
        return createTestMessage(threadId);
    }

    Message createTestMessage(String threadId) {
        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .build();

        return service.createMessage(threadId, messageRequest);
    }
}