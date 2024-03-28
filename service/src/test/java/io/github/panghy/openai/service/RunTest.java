package io.github.panghy.openai.service;

import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.assistants.Assistant;
import io.github.panghy.openai.assistants.AssistantRequest;
import io.github.panghy.openai.messages.Message;
import io.github.panghy.openai.messages.MessageRequest;
import io.github.panghy.openai.runs.Run;
import io.github.panghy.openai.runs.RunCreateRequest;
import io.github.panghy.openai.threads.Thread;
import io.github.panghy.openai.threads.ThreadRequest;
import io.github.panghy.openai.utils.TikTokensUtil;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RunTest {
    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    @Timeout(10)
    void createRetrieveRun() {
        AssistantRequest assistantRequest = AssistantRequest.builder()
                .model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName())
                .name("MATH_TUTOR")
                .instructions("You are a personal Math Tutor.")
                .build();
        Assistant assistant = service.createAssistant(assistantRequest);

        ThreadRequest threadRequest = ThreadRequest.builder()
                .build();
        Thread thread = service.createThread(threadRequest);

        MessageRequest messageRequest = MessageRequest.builder()
                .content("Hello")
                .build();

        Message message = service.createMessage(thread.getId(), messageRequest);

        RunCreateRequest runCreateRequest = RunCreateRequest.builder()
                .assistantId(assistant.getId())
                .build();

        Run run = service.createRun(thread.getId(), runCreateRequest);
        assertNotNull(run);

        Run retrievedRun;
        do {
            retrievedRun = service.retrieveRun(thread.getId(), run.getId());
            assertEquals(run.getId(), retrievedRun.getId());
        }
        while (!(retrievedRun.getStatus().equals("completed")) && !(retrievedRun.getStatus().equals("failed")));


        assertNotNull(retrievedRun);

        OpenAiResponse<Message> response = service.listMessages(thread.getId());

        List<Message> messages = response.getData();
        assertEquals(2, messages.size());
        assertEquals("user", messages.get(1).getRole());
        assertEquals("assistant", messages.get(0).getRole());
    }
}
