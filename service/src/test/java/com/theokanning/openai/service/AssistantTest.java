package com.theokanning.openai.service;

import com.theokanning.openai.assistants.AssistantBase;
import com.theokanning.openai.assistants.AssistantRequest;
import com.theokanning.openai.assistants.Assistant;
import com.theokanning.openai.assistants.AssistantToolsEnum;
import com.theokanning.openai.assistants.Tool;
import com.theokanning.openai.utils.TikTokensUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AssistantTest {
    String token = "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";

    OpenAiService service = new OpenAiService(token);

    @Test
    void createAssistant() {
        AssistantBase assistantRequest = AssistantRequest.builder()
                .model(TikTokensUtil.ModelEnum.GPT_4_1106_preview.getName())
                .name("Math Tutor")
                .instructions("You are a personal Math Tutor.")
                .tools(Collections.singletonList(new Tool(AssistantToolsEnum.CODE_INTERPRETER)))
                .build();

        Assistant assistantResponse = service.createAssistant(assistantRequest);
        System.out.println(assistantResponse.getTools().get(0));

        assertNotNull(assistantResponse);
        assertNotNull(assistantResponse.getId());
        assertNotNull(assistantResponse.getCreatedAt());
        assertNotNull(assistantResponse.getObject());
        assertEquals(assistantResponse.getTools().get(0).getType(),  AssistantToolsEnum.CODE_INTERPRETER);
        assertEquals(assistantRequest.getName(), assistantResponse.getName());
    }
}
