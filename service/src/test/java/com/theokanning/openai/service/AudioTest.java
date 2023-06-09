package com.theokanning.openai.service;

import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.CreateTranslationRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


public class AudioTest {

    static String englishAudioFilePath = "src/test/resources/hello-world.mp3";

    static String koreanAudioFilePath = "src/test/resources/korean-hello.mp3";

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token, Duration.ofSeconds(30));

    @Test
    void createTranscription() {
        CreateTranscriptionRequest createTranscriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .build();

        String text = service.createTranscription(createTranscriptionRequest, englishAudioFilePath).getText();
        assertEquals("Hello World.", text);
    }

    @Test
    void createTranscriptionVerbose() {
        CreateTranscriptionRequest createTranscriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .responseFormat("verbose_json")
                .build();

        TranscriptionResult result = service.createTranscription(createTranscriptionRequest, englishAudioFilePath);
        assertEquals("Hello World.", result.getText());
        assertEquals("transcribe", result.getTask());
        assertEquals("english", result.getLanguage());
        assertTrue(result.getDuration() > 0);
        assertEquals(1, result.getSegments().size());
    }

    @Test
    void createTranslation() {
        CreateTranslationRequest createTranslationRequest = CreateTranslationRequest.builder()
                .model("whisper-1")
                .build();

        String text = service.createTranslation(createTranslationRequest, koreanAudioFilePath).getText();
        assertEquals("Hello, my name is Yuna. I am Korean voice.", text);
    }

    @Test
    void createTranslationVerbose() {
        CreateTranslationRequest createTranslationRequest = CreateTranslationRequest.builder()
                .model("whisper-1")
                .responseFormat("verbose_json")
                .build();

        TranslationResult result = service.createTranslation(createTranslationRequest, koreanAudioFilePath);
        assertEquals("Hello, my name is Yuna. I am Korean voice.", result.getText());
        assertEquals("translate", result.getTask());
        assertEquals("english", result.getLanguage());
        assertTrue(result.getDuration() > 0);
        assertEquals(1, result.getSegments().size());
    }
}
