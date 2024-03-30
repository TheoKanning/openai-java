package io.github.panghy.openai.service;

import io.github.panghy.openai.OpenAiHttpException;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.edit.EditChoice;
import io.github.panghy.openai.edit.EditRequest;
import io.github.panghy.openai.edit.EditResult;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EditTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void edit() throws OpenAiHttpException {
        EditRequest request = EditRequest.builder()
                .model("text-davinci-edit-001")
                .input("What day of the wek is it?")
                .instruction("Fix the spelling mistakes")
                .build();

        when(mockApi.createEdit(request)).thenReturn(just(EditResult.builder().
            choices(List.of(EditChoice.builder().
                text("What day of the week is it?").
                build())).
            build()));
        EditResult result = service.createEdit(request);
        assertNotNull(result.getChoices().get(0).getText());
    }
}
