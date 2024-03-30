package io.github.panghy.openai.service;

import io.github.panghy.openai.OpenAiResponse;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.model.Model;
import io.github.panghy.openai.service.OpenAiService;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


public class ModelTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void listModels() {
        when(mockApi.listModels()).thenReturn(just(OpenAiResponse.<Model>builder().data(List.of(
            Model.builder().id("babbage-002").ownedBy("system").build(),
            Model.builder().id("babbage-003").ownedBy("system").build()
        )).build()));
        List<Model> models = service.listModels();
        verify(mockApi, times(1)).listModels();

        assertFalse(models.isEmpty());
    }

    @Test
    void getModel() {
        when(mockApi.getModel("babbage-002")).thenReturn(
            just(Model.builder().id("babbage-002").ownedBy("system").build()));
        Model model = service.getModel("babbage-002");
        verify(mockApi, times(1)).getModel("babbage-002");

        assertEquals("babbage-002", model.id);
        assertEquals("system", model.ownedBy);
    }
}
