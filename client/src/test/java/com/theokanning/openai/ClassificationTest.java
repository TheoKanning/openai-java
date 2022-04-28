package com.theokanning.openai;

import com.theokanning.openai.classification.ClassificationRequest;
import com.theokanning.openai.classification.ClassificationResult;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ClassificationTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createCompletion() {
        ClassificationRequest classificationRequest = ClassificationRequest.builder()
                .examples(Arrays.asList(
                        Arrays.asList("A happy moment", "Positive"),
                        Arrays.asList("I am sad.", "Negative"),
                        Arrays.asList("I am feeling awesome", "Positive")
                ))
                .query("It is a raining day :(")
                .model("curie")
                .searchModel("ada")
                .labels(Arrays.asList("Positive", "Negative", "Neutral"))
                .build();

        ClassificationResult result = service.createClassification(classificationRequest);

        assertNotNull(result.getCompletion());
    }
}
