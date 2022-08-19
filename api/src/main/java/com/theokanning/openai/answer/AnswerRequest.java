package com.theokanning.openai.answer;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Given a question, a set of documents, and some examples, the API generates an answer to the question based
 * on the information in the set of documents. This is useful for question-answering applications on sources of truth,
 * like company documentation or a knowledge base.
 *
 * Documentation taken from
 * https://beta.openai.com/docs/api-reference/answers/create
 */
@Deprecated
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnswerRequest {

    /**
     * ID of the engine to use for completion.
     */
    @NonNull
    String model;

    /**
     * Question to get answered.
     */
    @NonNull
    String question;

    /**
     * List of (question, answer) pairs that will help steer the model towards the tone and answer format you'd like.
     * We recommend adding 2 to 3 examples.
     */
    @NonNull
    List<List<String>> examples;

    /**
     * A text snippet containing the contextual information used to generate the answers for the examples you provide.
     */
    @NonNull
    String examplesContext;

    /**
     * List of documents from which the answer for the input question should be derived.
     * If this is an empty list, the question will be answered based on the question-answer examples.
     *
     * You should specify either documents or a file, but not both.
     */
    List<String> documents;

    /**
     * The ID of an uploaded file that contains documents to search over.
     * See upload file for how to upload a file of the desired format and purpose.
     *
     * You should specify either documents or file, but not both.
     */
    String file;

    /**
     * ID of the engine to use for Search. You can select one of ada, babbage, curie, or davinci.
     */
    String searchModel;

    /**
     * The maximum number of documents to be ranked by Search when using file.
     * Setting it to a higher value leads to improved accuracy but with increased latency and cost.
     */
    Integer maxRerank;

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
     * The maximum number of tokens allowed for the generated answer.
     */
    Integer maxTokens;

    /**
     * Up to 4 sequences where the API will stop generating further tokens.
     * The returned text will not contain the stop sequence.
     */
    List<String> stop;

    /**
     * How many answers to generate for each question.
     */
    Integer n;

    /**
     * Modify the likelihood of specified tokens appearing in the completion.
     *
     * Accepts a json object that maps tokens (specified by their token ID in the GPT tokenizer) to an
     * associated bias value from -100 to 100.
     */
    Map<String, Double> logitBias;

    /**
     * A special boolean flag for showing metadata.
     * If set to true, each document entry in the returned JSON will contain a "metadata" field.
     *
     * This flag only takes effect when file is set.
     */
    Boolean returnMetadata;

    /**
     * If set to true, the returned JSON will include a "prompt" field containing the final prompt that was
     * used to request a completion. This is mainly useful for debugging purposes.
     */
    Boolean returnPrompt;

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
