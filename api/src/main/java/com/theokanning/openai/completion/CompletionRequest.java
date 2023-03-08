package com.theokanning.openai.completion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * A request for OpenAi to generate a predicted completion for a prompt.
 * All fields are nullable.
 *
 * https://beta.openai.com/docs/api-reference/completions/create
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompletionRequest {

    /**
     * The name of the model to use.
     * Required if specifying a fine tuned model or if using the new v1/completions endpoint.
     */
    String model;

    /**
     * An optional prompt to complete from
     */
    String prompt;

    /**
     * The suffix that comes after a completion of inserted text.
     */
    String suffix;

    /**
     * The maximum number of tokens to generate.
     * Requests can use up to 2048 tokens shared between prompt and completion.
     * (One token is roughly 4 characters for normal English text)
     */
    Integer maxTokens;

    /**
     * What sampling temperature to use. Higher values means the model will take more risks.
     * Try 0.9 for more creative applications, and 0 (argmax sampling) for ones with a well-defined answer.
     *
     * We generally recommend using this or {@link CompletionRequest#topP} but not both.
     */
    Double temperature;

    /**
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of
     * the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are
     * considered.
     *
     * We generally recommend using this or {@link CompletionRequest#temperature} but not both.
     */
    Double topP;

    /**
     * How many completions to generate for each prompt.
     *
     * Because this parameter generates many completions, it can quickly consume your token quota.
     * Use carefully and ensure that you have reasonable settings for {@link CompletionRequest#maxTokens} and {@link CompletionRequest#stop}.
     */
    Integer n;

    /**
     * Whether to stream back partial progress.
     * If set, tokens will be sent as data-only server-sent events as they become available,
     * with the stream terminated by a data: DONE message.
     */
    Boolean stream;

    /**
     * Include the log probabilities on the logprobs most likely tokens, as well the chosen tokens.
     * For example, if logprobs is 10, the API will return a list of the 10 most likely tokens.
     * The API will always return the logprob of the sampled token,
     * so there may be up to logprobs+1 elements in the response.
     */
    Integer logprobs;

    /**
     * Echo back the prompt in addition to the completion
     */
    Boolean echo;

    /**
     * Up to 4 sequences where the API will stop generating further tokens.
     * The returned text will not contain the stop sequence.
     */
    List<String> stop;

    /**
     * Number between 0 and 1 (default 0) that penalizes new tokens based on whether they appear in the text so far.
     * Increases the model's likelihood to talk about new topics.
     */
    Double presencePenalty;

    /**
     * Number between 0 and 1 (default 0) that penalizes new tokens based on their existing frequency in the text so far.
     * Decreases the model's likelihood to repeat the same line verbatim.
     */
    Double frequencyPenalty;

    /**
     * Generates best_of completions server-side and returns the "best"
     * (the one with the lowest log probability per token).
     * Results cannot be streamed.
     *
     * When used with {@link CompletionRequest#n}, best_of controls the number of candidate completions and n specifies how many to return,
     * best_of must be greater than n.
     */
    Integer bestOf;

    /**
     * Modify the likelihood of specified tokens appearing in the completion.
     *
     * Maps tokens (specified by their token ID in the GPT tokenizer) to an associated bias value from -100 to 100.
     *
     * https://beta.openai.com/docs/api-reference/completions/create#completions/create-logit_bias
     */
    Map<String, Integer> logitBias;

    /**
     * A unique identifier representing your end-user, which will help OpenAI to monitor and detect abuse.
     */
    String user;
}
