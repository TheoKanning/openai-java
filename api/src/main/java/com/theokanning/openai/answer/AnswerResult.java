package com.theokanning.openai.answer;

import lombok.Data;

import java.util.List;

/**
 * An object containing a response from the answer api
 *
 * https://beta.openai.com/docs/api-reference/answers/create
 */
@Data
public class AnswerResult {
    /**
     * A list of generated answers to the provided question/
     */
    List<String> answers;

    /**
     * A unique id assigned to this completion
     */
    String completion;

    /**
     * The GPT-3 model used for completion
     */
    String model;

    /**
     * The type of object returned, should be "answer"
     */
    String object;

    /**
     * The GPT-3 model used for search
     */
    String searchModel;

    /**
     * A list of the most relevant documents for the question.
     */
    List<Document> selectedDocuments;
}
