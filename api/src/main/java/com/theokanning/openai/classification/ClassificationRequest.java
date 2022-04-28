package com.theokanning.openai.classification;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * A request for OpenAi to classify text based on provided examples
 * All fields are nullable.
 *
 * Documentation taken from
 * https://beta.openai.com/docs/api-reference/classifications/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassificationRequest {

    /**
     * ID of the engine to use for completion
     */
    @NonNull
    String model;

    /**
     * Query to be classified
     */
    @NonNull
    String query;

    /**
     * A list of examples with labels, in the following format:
     *
     * [["The movie is so interesting.", "Positive"], ["It is quite boring.", "Negative"], ...]
     *
     * All the label strings will be normalized to be capitalized.
     *
     * You should specify either examples or file, but not both.
     */
    List<List<String>> examples;

    /**
     * The ID of the uploaded file that contains training examples.
     * See upload file for how to upload a file of the desired format and purpose.
     *
     * You should specify either examples or file, but not both.
     */
    String file;

    /**
     * The set of categories being classified.
     * If not specified, candidate labels will be automatically collected from the examples you provide.
     * All the label strings will be normalized to be capitalized.
     */
    List<String> labels;

    /**
     * ID of the engine to use for Search. You can select one of ada, babbage, curie, or davinci.
     */
    String searchModel;

    /**
     * What sampling temperature to use. Higher values means the model will take more risks.
     * Try 0.9 for more creative applications, and 0 (argmax sampling) for ones with a well-defined answer.
     *
     * We generally recommend using this or {@link top_p} but not both.
     */
    Double temperature;

    /**
     * Include the log probabilities on the logprobs most likely tokens, as well the chosen tokens.
     * For example, if logprobs is 10, the API will return a list of the 10 most likely tokens.
     * The API will always return the logprob of the sampled token,
     * so there may be up to logprobs+1 elements in the response.
     */
    Integer logprobs;

    /**
     * The maximum number of examples to be ranked by Search when using file.
     * Setting it to a higher value leads to improved accuracy but with increased latency and cost.
     */
    Integer maxExamples;

    /**
     * Modify the likelihood of specified tokens appearing in the completion.
     *
     * Accepts a json object that maps tokens (specified by their token ID in the GPT tokenizer) to an
     * associated bias value from -100 to 100.
     */
    Map<String, Double> logitBias;

    /**
     * If set to true, the returned JSON will include a "prompt" field containing the final prompt that was
     * used to request a completion. This is mainly useful for debugging purposes.
     */
    Boolean returnPrompt;

    /**
     * A special boolean flag for showing metadata.
     * If set to true, each document entry in the returned JSON will contain a "metadata" field.
     *
     * This flag only takes effect when file is set.
     */
    Boolean returnMetadata;

    /**
     * If an object name is in the list, we provide the full information of the object;
     * otherwise, we only provide the object ID.
     *
     * Currently we support completion and file objects for expansion.
     */
    List<String> expand;

    /**
     * A unique identifier representing your end-user, which will help OpenAI to monitor and detect abuse.
     */
    String user;
}
