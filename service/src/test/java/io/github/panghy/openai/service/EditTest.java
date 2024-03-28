package io.github.panghy.openai.service;

import io.github.panghy.openai.OpenAiHttpException;
import io.github.panghy.openai.edit.EditRequest;
import io.github.panghy.openai.edit.EditResult;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EditTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void edit() throws OpenAiHttpException {
        EditRequest request = EditRequest.builder()
                .model("text-davinci-edit-001")
                .input("What day of the wek is it?")
                .instruction("Fix the spelling mistakes")
                .build();

        EditResult result = service.createEdit(request);
        assertNotNull(result.getChoices().get(0).getText());
    }
}
