package com.theokanning.openai.classification;

import com.theokanning.openai.completion.CompletionChoice;
import lombok.Data;

import java.util.List;

/**
 * An object containing a response from the classification api
 * <p>
 * https://beta.openai.com/docs/api-reference/classifications/create
 */
@Data
public class ClassificationResult {

    /**
     * A unique id assigned to this completion
     */
    String completion;

    /**
     * The predicted label for the query text.
     */
    String label;

    /**
     * The GPT-3 model used for completion
     */
    String model;

    /**
     * The type of object returned, should be "classification"
     */
    String object;

    /**
     * The GPT-3 model used for search
     */
    String searchModel;

    /**
     * A list of the most relevant examples for the query text.
     */
    List<Example> selectedExamples;
}
