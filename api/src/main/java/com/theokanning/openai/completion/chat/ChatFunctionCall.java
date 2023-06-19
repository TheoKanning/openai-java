package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatFunctionCall {

    String name;

    JsonNode arguments;

}
