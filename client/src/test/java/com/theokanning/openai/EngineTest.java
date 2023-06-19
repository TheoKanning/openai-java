package com.theokanning.openai;

import com.theokanning.openai.client.OpenAiService;
import com.theokanning.openai.engine.Engine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class EngineTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void getEngines() {
        List<Engine> engines = service.getEngines();

        assertFalse(engines.isEmpty());
    }

    @Test
    void getEngine() {
        Engine ada = service.getEngine("ada");

        assertEquals("ada", ada.id);
    }
}
