package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequest {

    /**
     * ID of the model to use.
     */
    String model;

    /**
     * The messages to generate chat completions for, in the <a
     * href="https://platform.openai.com/docs/guides/chat/introduction">chat format</a>.<br>
     * see {@link ChatMessage}
     */
    List<ChatMessage> messages;

    /**
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random, while lower
     * values like 0.2 will make it more focused and deterministic.<br>
     * We generally recommend altering this or top_p but not both.
     */
    Double temperature;

    /**
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens
     * with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.<br>
     * We generally recommend altering this or temperature but not both.
     */
    @JsonProperty("top_p")
    Double topP;

    /**
     * How many chat completion chatCompletionChoices to generate for each input message.
     */
    Integer n;

    /**
     * For consistent results, use the same seed and parameters in your request.
     * Set the seed parameter to an integer of your choice and maintain its value across requests for deterministic outputs.
     *
     * Determinism cannot be guaranteed, so please refer to the {@link ChatCompletionResult#fingerprint} to track any changes in the backend.
     */
    Integer seed;

    /**
     * If set, partial message deltas will be sent, like in ChatGPT. Tokens will be sent as data-only <a
     * href="https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events#Event_stream_format">server-sent
     * events</a> as they become available, with the stream terminated by a data: [DONE] message.
     */
    Boolean stream;

    /**
     * Up to 4 sequences where the API will stop generating further tokens.
     */
    List<String> stop;

    /**
     * The maximum number of tokens allowed for the generated answer. By default, the number of tokens the model can return will
     * be (4096 - prompt tokens).
     */
    @JsonProperty("max_tokens")
    Integer maxTokens;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far,
     * increasing the model's likelihood to talk about new topics.
     */
    @JsonProperty("presence_penalty")
    Double presencePenalty;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far,
     * decreasing the model's likelihood to repeat the same line verbatim.
     */
    @JsonProperty("frequency_penalty")
    Double frequencyPenalty;

    /**
     * Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100
     * to 100. Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will
     * vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100
     * should result in a ban or exclusive selection of the relevant token.
     */
    @JsonProperty("logit_bias")
    Map<String, Integer> logitBias;


    /**
     * A unique identifier representing your end-user, which will help OpenAI to monitor and detect abuse.
     */
    String user;

    /**
     * A list of the available functions.
     */
    List<?> functions;

    /**
     * Controls how the model responds to function calls, as specified in the <a href="https://platform.openai.com/docs/api-reference/chat/create#chat/create-function_call">OpenAI documentation</a>.
     */
    @JsonProperty("function_call")
    ChatCompletionRequestFunctionCall functionCall;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatCompletionRequestFunctionCall {
        String name;

        public static ChatCompletionRequestFunctionCall of(String name) {
            return new ChatCompletionRequestFunctionCall(name);
        }

    }
}
