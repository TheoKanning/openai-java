package com.theokanning.openai.service;

import com.theokanning.openai.model.Model;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class ModelTest {

    String token = System.getenv("OPENAI_TOKEN");
    com.theokanning.openai.service.OpenAiService service = new OpenAiService(token);

    @Test
    void listModels() {
        List<Model> models = service.listModels();

        assertFalse(models.isEmpty());
    }

    @Test
    void getModel() {
        Model ada = service.getModel("ada");

        assertEquals("ada", ada.id);
        assertEquals("openai", ada.ownedBy);
        assertFalse(ada.permission.isEmpty());
    }
}
