package com.theokanning.openai.completion.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatFunctionCall {

    String name;
    Map<String, Object> arguments;

}
