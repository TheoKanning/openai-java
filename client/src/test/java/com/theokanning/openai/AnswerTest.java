package com.theokanning.openai;

import com.theokanning.openai.answer.AnswerRequest;
import com.theokanning.openai.answer.AnswerResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AnswerTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createAnswer() {
        AnswerRequest answerRequest = AnswerRequest.builder()
                .documents(Arrays.asList("Puppy A is happy.", "Puppy B is sad."))
                .question("which puppy is happy?")
                .searchModel("ada")
                .model("curie")
                .examplesContext("In 2017, U.S. life expectancy was 78.6 years.")
                .examples(Collections.singletonList(
                        Arrays.asList("What is human life expectancy in the United States?", "78 years.")
                ))
                .maxTokens(5)
                .stop(Arrays.asList("\n", "<|endoftext|>"))
                .build();

        AnswerResult result = service.createAnswer(answerRequest);

        assertNotNull(result.getAnswers().get(0));
    }
}
