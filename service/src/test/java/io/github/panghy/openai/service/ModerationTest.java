package io.github.panghy.openai.service;

import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.moderation.Moderation;
import io.github.panghy.openai.moderation.ModerationCategoryScores;
import io.github.panghy.openai.moderation.ModerationRequest;
import io.github.panghy.openai.moderation.ModerationResult;
import io.github.panghy.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ModerationTest {

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void createModeration() {
        ModerationRequest moderationRequest = ModerationRequest.builder()
                .input("I want to kill him")
                .model("text-moderation-latest")
                .build();

        when(mockApi.createModeration(moderationRequest)).thenReturn(just(ModerationResult.builder()
            .results(List.of(
                Moderation.builder().flagged(true).build()
            ))
            .build()));
        Moderation moderationScore = service.createModeration(moderationRequest).getResults().get(0);

        assertTrue(moderationScore.isFlagged());
    }
}
