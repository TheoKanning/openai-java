package com.theokanning.openai.completion.chat;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * A chat completion generated by OpenAI
 */
@Data
public class ChatCompletionChoice {

    /**
     * This index of this completion in the returned list.
     */
    Integer index;

    /**
     * The {@link ChatMessageRole#ASSISTANT} message or delta (when streaming) which was generated
     */
    @JsonAlias("delta")
    ChatMessage<String> message;

    /**
     * The reason why GPT stopped generating, for example "length".
     */
    @JsonProperty("finish_reason")
    String finishReason;

    /**
     * When use the GPT-4V model, this will be return, for example {"type":"stop","stop":"<|fim_suffix|>"}.
     */
    @JsonProperty("finish_details")
    FinishDetails finishDetails;
}
