package io.github.panghy.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.completion.*;
import io.github.panghy.openai.service.OpenAiService;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CompletionTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void createCompletion() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("babbage-002")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .n(5)
                .maxTokens(50)
                .user("testing")
                .logitBias(new HashMap<>())
                .logprobs(5)
                .build();

        when(mockApi.createCompletion(completionRequest)).thenReturn(just(CompletionResult.builder().
            id("cmpl-123").
            object("text_completion").
            model("babbage-002").
            choices(List.of(
                CompletionChoice.builder().
                    text("I ain't the sharpest tool in the shed").
                    index(0).
                    logprobs(LogProbResult.builder().build()).
                    finish_reason("length").
                    build(),
                CompletionChoice.builder().
                    text("She was looking kind of dumb with her finger and her thumb").
                    index(1).
                    logprobs(LogProbResult.builder().build()).
                    finish_reason("length").
                    build(),
                CompletionChoice.builder().
                    text("In the shape of an L on her forehead").
                    index(2).
                    logprobs(LogProbResult.builder().build()).
                    finish_reason("length").
                    build(),
                CompletionChoice.builder().
                    text("Well, the years start coming and they don't stop coming").
                    index(3).
                    logprobs(LogProbResult.builder().build()).
                    finish_reason("length").
                    build(),
                CompletionChoice.builder().
                    text("Fed to the rules and I hit the ground running").
                    index(4).
                    logprobs(LogProbResult.builder().build()).
                    finish_reason("length").
                    build()
            )).
            build()));
        List<CompletionChoice> choices = service.createCompletion(completionRequest).getChoices();
        assertEquals(5, choices.size());
        assertNotNull(choices.get(0).getLogprobs());
    }

    @Test
    void streamCompletion() throws JsonProcessingException {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("babbage-002")
                .prompt("Somebody once told me the world is gonna roll me")
                .echo(true)
                .n(1)
                .maxTokens(25)
                .user("testing")
                .logitBias(new HashMap<>())
                .logprobs(5)
                .stream(true)
                .build();

        List<CompletionChunk> chunks = new ArrayList<>();
        CompletionChunk chunk = CompletionChunk.builder().
            id("cmpl-123").
            object("text_completion").
            model("babbage-002").
            choices(List.of(
                CompletionChoice.builder().
                    text("I ain't the sharpest tool in the shed").
                    index(0).
                    logprobs(LogProbResult.builder().build()).
                    finish_reason("length").
                    build()
            )).
            build();
        ObjectMapper mapper = new ObjectMapper();
        when(mockApi.createCompletionStream(completionRequest)).
            thenReturn(new FakeCall<>(ResponseBody.create(MediaType.parse("text/event-stream"),
            "data: " + mapper.writeValueAsString(chunk) + "\n\n")));
        service.streamCompletion(completionRequest).blockingForEach(chunks::add);
        assertTrue(chunks.size() > 0);
        assertNotNull(chunks.get(0).getChoices().get(0));
    }
}
