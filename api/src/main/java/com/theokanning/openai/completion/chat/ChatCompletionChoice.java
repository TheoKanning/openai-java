package com.theokanning.openai.completion.chat;
import lombok.Data;

/**
 * A chat completion generated by GPT-3.5
 */
@Data
public class ChatCompletionChoice {

    /**
     * This index of this completion in the returned list.
     */
    Integer index;

    /**
     * The {@link ChatMessageRole#assistant} message which was generated.
     */
    ChatMessage message;

    /**
     * The reason why GPT-3 stopped generating, for example "length".
     */
    String finishReason;
}