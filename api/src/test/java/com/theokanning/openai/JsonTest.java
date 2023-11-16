package com.theokanning.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.edit.EditResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.engine.Engine;
import com.theokanning.openai.file.File;
import com.theokanning.openai.fine_tuning.FineTuningEvent;
import com.theokanning.openai.fine_tuning.FineTuningJob;
import com.theokanning.openai.fine_tuning.FineTuningJobRequest;
import com.theokanning.openai.finetune.FineTuneEvent;
import com.theokanning.openai.finetune.FineTuneResult;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.messages.Message;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.moderation.ModerationResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    @ParameterizedTest
    @ValueSource(classes = {
            ChatCompletionRequest.class,
            ChatCompletionResult.class,
            DeleteResult.class,
            EditRequest.class,
            EditResult.class,
            EmbeddingRequest.class,
            EmbeddingResult.class,
            Engine.class,
            File.class,
            FineTuneEvent.class,
            FineTuneResult.class,
            FineTuningEvent.class,
            FineTuningJob.class,
            FineTuningJobRequest.class,
            ImageResult.class,
            TranscriptionResult.class,
            TranslationResult.class,
            Message.class,
            Model.class,
            ModerationRequest.class,
            ModerationResult.class
    })
    void objectMatchesJson(Class<?> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String path = "src/test/resources/fixtures/" + clazz.getSimpleName() + ".json";
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String json = new String(bytes);

        String actual = mapper.writeValueAsString(mapper.readValue(json, clazz));

        // Convert to JsonNodes to avoid any json formatting differences
        assertEquals(mapper.readTree(json), mapper.readTree(actual));
    }
}
