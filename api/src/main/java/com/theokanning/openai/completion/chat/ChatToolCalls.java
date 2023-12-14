package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatToolCalls {

    /**
     * The ID of the tool call
     */
    String id;

    /**
     * The type of the tool. Currently, only function is supported.
     */
    String type;

    /**
     * The function that the model called.
     */
    ChatFunctionCall function;

}
