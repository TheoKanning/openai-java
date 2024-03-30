package io.github.panghy.openai.service;

import io.github.panghy.openai.audio.*;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.service.OpenAiService;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AudioTest {

    static String englishAudioFilePath = "src/test/resources/hello-world.mp3";
    static String koreanAudioFilePath = "src/test/resources/korean-hello.mp3";

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void createTranscription() {
        CreateTranscriptionRequest createTranscriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .build();

        mock(OpenAiService.class);
        when(mockApi.createTranscription(any())).thenReturn(just(TranscriptionResult.builder()
            .text("Hello World.")
            .build()));
        String text = service.createTranscription(createTranscriptionRequest, englishAudioFilePath).getText();
        verify(mockApi, times(1)).createTranscription(any());
        assertEquals("Hello World.", text);
    }

    @Test
    void createTranscriptionVerbose() {
        CreateTranscriptionRequest createTranscriptionRequest = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .responseFormat("verbose_json")
                .build();

        when(mockApi.createTranscription(any())).thenReturn(just(TranscriptionResult.builder()
            .text("Hello World.")
            .task("transcribe")
            .language("english")
            .duration(1.0)
            .segments(
                List.of(TranscriptionSegment.builder()
                    .text("Hello World.")
                    .start(0.0)
                    .end(1.0)
                    .build()))
            .build()));
        TranscriptionResult result = service.createTranscription(createTranscriptionRequest, englishAudioFilePath);
        verify(mockApi, times(1)).createTranscription(any());
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

        mock(OpenAiService.class);
        when(mockApi.createTranslation(any())).thenReturn(just(TranslationResult.builder()
            .text("Hello, my name is Yoona. I am a Korean native speaker.")
            .build()));
        String text = service.createTranslation(createTranslationRequest, koreanAudioFilePath).getText();
        verify(mockApi, times(1)).createTranslation(any());
        assertEquals("Hello, my name is Yoona. I am a Korean native speaker.", text);
    }

    @Test
    void createTranslationVerbose() {
        CreateTranslationRequest createTranslationRequest = CreateTranslationRequest.builder()
                .model("whisper-1")
                .responseFormat("verbose_json")
                .build();

        mock(OpenAiService.class);
        when(mockApi.createTranslation(any())).thenReturn(just(TranslationResult.builder()
            .text("Hello, my name is Yoona. I am a Korean native speaker.")
            .task("translate")
            .language("english")
            .duration(1.0)
            .segments(
                List.of(TranscriptionSegment.builder()
                    .text("Hello, my name is Yoona. I am a Korean native speaker.")
                    .start(0.0)
                    .end(1.0)
                    .build()))
            .build()));
        TranslationResult result = service.createTranslation(createTranslationRequest, koreanAudioFilePath);
        verify(mockApi, times(1)).createTranslation(any());
        assertEquals("Hello, my name is Yoona. I am a Korean native speaker.", result.getText());
        assertEquals("translate", result.getTask());
        assertEquals("english", result.getLanguage());
        assertTrue(result.getDuration() > 0);
        assertEquals(1, result.getSegments().size());
    }

    @Test
    void createSpeech() throws IOException {
        CreateSpeechRequest createSpeechRequest = CreateSpeechRequest.builder()
                .model("tts-1")
                .input("Hello World.")
                .voice("alloy")
                .build();

        when(mockApi.createSpeech(any())).thenReturn(just(ResponseBody.create(MediaType.get("audio/mpeg"),
            new byte[]{1})));
        final ResponseBody speech = service.createSpeech(createSpeechRequest);
        verify(mockApi, times(1)).createSpeech(any());
        assertNotNull(speech);
        assertEquals(MediaType.get("audio/mpeg"), speech.contentType());
        assertTrue(speech.bytes().length > 0);
    }
}
