package io.github.panghy.openai.service;

import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.assistants.Assistant;
import io.github.panghy.openai.assistants.AssistantRequest;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.messages.Message;
import io.github.panghy.openai.messages.MessageRequest;
import io.github.panghy.openai.runs.Run;
import io.github.panghy.openai.runs.RunCreateRequest;
import io.github.panghy.openai.threads.Thread;
import io.github.panghy.openai.threads.ThreadRequest;
import io.github.panghy.openai.utils.TikTokensUtil;
import io.github.panghy.openai.service.OpenAiService;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RunTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    @Timeout(10)
    void createRetrieveRun() {
        AssistantRequest assistantRequest = AssistantRequest.builder()
                .model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName())
                .name("MATH_TUTOR")
                .instructions("You are a personal Math Tutor.")
                .build();
        when(mockApi.createAssistant(assistantRequest)).thenReturn(
            just(Assistant.builder().id("assistant-id").model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName())
                .build()));
        Assistant assistant = service.createAssistant(assistantRequest);
        verify(mockApi, times(1)).createAssistant(assistantRequest);

        ThreadRequest threadRequest = ThreadRequest.builder()
                .build();
        when(mockApi.createThread(threadRequest)).thenReturn(
            just(Thread.builder().id("thread-id").build()));
        Thread thread = service.createThread(threadRequest);
        verify(mockApi, times(1)).createThread(threadRequest);

        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .build();

        when(mockApi.createMessage(thread.getId(), messageRequest)).thenReturn(
            just(Message.builder()
                .id("message-id")
                .object("thread.message")
                .role("user")
                .build()));
        Message message = service.createMessage(thread.getId(), messageRequest);
        verify(mockApi, times(1)).createMessage(thread.getId(), messageRequest);

        RunCreateRequest runCreateRequest = RunCreateRequest.builder()
                .assistantId(assistant.getId())
                .build();

        when(mockApi.createRun(thread.getId(), runCreateRequest)).thenReturn(
            just(Run.builder()
                .id("run-id")
                .status("running")
                .build()));
        Run run = service.createRun(thread.getId(), runCreateRequest);
        verify(mockApi, times(1)).createRun(thread.getId(), runCreateRequest);
        assertNotNull(run);

        Run retrievedRun;
        do {
            when(mockApi.retrieveRun(thread.getId(), run.getId())).thenReturn(
                just(Run.builder()
                    .id("run-id")
                    .status("completed")
                    .build()));
            retrievedRun = service.retrieveRun(thread.getId(), run.getId());
            verify(mockApi, atLeastOnce()).retrieveRun(thread.getId(), run.getId());
            assertEquals(run.getId(), retrievedRun.getId());
        }
        while (!(retrievedRun.getStatus().equals("completed")) && !(retrievedRun.getStatus().equals("failed")));

        assertNotNull(retrievedRun);

        when(mockApi.listMessages(thread.getId())).thenReturn(
            just(OpenAiResponse.<Message>builder().data(List.of(
                Message.builder().id("message-id").role("user").build(),
                Message.builder().id("message-id").role("assistant").build()
            )).build()));
        OpenAiResponse<Message> response = service.listMessages(thread.getId());

        List<Message> messages = response.getData();
        assertEquals(2, messages.size());
        assertEquals("user", messages.get(0).getRole());
        assertEquals("assistant", messages.get(1).getRole());
    }
}
