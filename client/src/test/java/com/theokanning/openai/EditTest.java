package com.theokanning.openai;

import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.edit.EditResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled // disabled until edit example CURL works
public class EditTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void edit() {
        EditRequest request = EditRequest.builder()
                .input("What day of the wek is it?")
                .instruction("Fix the spelling mistakes")
                .build();

        EditResult result = service.createEdit("text-ada-001", request);

        assertEquals("What day of the week is it?", result.getChoices().get(0).getText());
    }
}
