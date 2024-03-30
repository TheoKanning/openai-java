package io.github.panghy.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import io.github.panghy.openai.audio.TranscriptionResult;
import io.github.panghy.openai.audio.TranslationResult;
import io.github.panghy.openai.completion.chat.ChatCompletionRequest;
import io.github.panghy.openai.completion.chat.ChatCompletionResult;
import io.github.panghy.openai.edit.EditRequest;
import io.github.panghy.openai.edit.EditResult;
import io.github.panghy.openai.embedding.EmbeddingRequest;
import io.github.panghy.openai.embedding.EmbeddingResult;
import io.github.panghy.openai.engine.Engine;
import io.github.panghy.openai.file.File;
import io.github.panghy.openai.fine_tuning.FineTuningEvent;
import io.github.panghy.openai.fine_tuning.FineTuningJob;
import io.github.panghy.openai.fine_tuning.FineTuningJobRequest;
import io.github.panghy.openai.finetune.FineTuneEvent;
import io.github.panghy.openai.finetune.FineTuneResult;
import io.github.panghy.openai.image.ImageResult;
import io.github.panghy.openai.messages.Message;
import io.github.panghy.openai.model.Model;
import io.github.panghy.openai.moderation.ModerationRequest;
import io.github.panghy.openai.moderation.ModerationResult;
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
