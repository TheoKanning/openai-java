package com.theokanning.openai;

import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.edit.EditResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EditTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void edit() {
        EditRequest request = EditRequest.builder()
                .input("What day of the wek is it?")
                .instruction("Fix the spelling mistakes")
                .build();

        EditResult result = service.createEdit("text-davinci-edit-001", request);

        assertNotNull(result.getChoices().get(0).getText());
    }
}
