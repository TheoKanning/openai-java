package com.theokanning.openai;

import com.theokanning.openai.moderation.Moderation;
import com.theokanning.openai.moderation.ModerationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ModerationTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createModeration() {
        String[] inputs = {"I want to kill them.", "I miss you."};

        ModerationRequest moderationRequest = ModerationRequest.builder()
                .input(inputs)
                .model("text-moderation-latest")
                .build();

        Moderation moderationScore = service.createModeration(moderationRequest).getResults().get(0);

        assertTrue(moderationScore.isFlagged());
    }
}
