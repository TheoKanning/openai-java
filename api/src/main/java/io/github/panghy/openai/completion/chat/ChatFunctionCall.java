package io.github.panghy.openai.completion.chat;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class ChatFunctionCall {

    /**
     * The name of the function being called
     */
    String name;

    /**
     * The arguments of the call produced by the model, represented as a JsonNode for easy manipulation.
     */
    JsonNode arguments;

}
