package io.github.panghy.openai.service;

import io.github.panghy.openai.model.Model;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class ModelTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void listModels() {
        List<Model> models = service.listModels();

        assertFalse(models.isEmpty());
    }

    @Test
    void getModel() {
        Model model = service.getModel("babbage-002");

        assertEquals("babbage-002", model.id);
        assertEquals("system", model.ownedBy);
    }
}
