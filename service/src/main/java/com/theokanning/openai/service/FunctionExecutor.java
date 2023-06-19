package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.theokanning.openai.completion.chat.ChatFunction;
import com.theokanning.openai.completion.chat.ChatFunctionCall;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;

import java.util.*;

public class FunctionExecutor {

    private ObjectMapper MAPPER = new ObjectMapper();
    private final Map<String, ChatFunction> FUNCTIONS = new HashMap<>();

    public FunctionExecutor(List<ChatFunction> functions) {
        setFunctions(functions);
    }

    public FunctionExecutor(List<ChatFunction> functions, ObjectMapper objectMapper) {
        setFunctions(functions);
        setObjectMapper(objectMapper);
    }

    public Optional<ChatMessage> executeAndConvertToMessageSafely(ChatFunctionCall call) {
        try {
            return Optional.ofNullable(executeAndConvertToMessage(call));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    public ChatMessage executeAndConvertToMessageHandlingExceptions(ChatFunctionCall call) {
        try {
            return executeAndConvertToMessage(call);
        } catch (Exception exception) {
            exception.printStackTrace();
            return convertExceptionToMessage(exception);
        }
    }

    public ChatMessage convertExceptionToMessage(Exception exception) {
        String error = exception.getMessage() == null ? exception.toString() : exception.getMessage();
        return new ChatMessage(ChatMessageRole.FUNCTION.value(), "{\"error\": \"" + error + "\"}", "error");
    }

    public ChatMessage executeAndConvertToMessage(ChatFunctionCall call) {
        return new ChatMessage(ChatMessageRole.FUNCTION.value(), executeAndConvertToJson(call), call.getName());
    }

    public String executeAndConvertToJson(ChatFunctionCall call) {
        try {
            Object execution = execute(call);
            return execution instanceof TextNode ? ((TextNode) execution).asText() : (execution instanceof String ? execution.toString() : MAPPER.writeValueAsString(execution));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object execute(ChatFunctionCall call) {
        ChatFunction function = FUNCTIONS.get(call.getName());
        Object obj = null;
        try {
            JsonNode arguments = call.getArguments();
            obj = MAPPER.readValue(arguments instanceof TextNode ? arguments.asText() : arguments.toPrettyString(), function.getParametersClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return function.getExecutor().apply(obj);
    }

    public List<ChatFunction> getFunctions() {
        return new ArrayList<>(FUNCTIONS.values());
    }

    public void setFunctions(List<ChatFunction> functions) {
        this.FUNCTIONS.clear();
        functions.forEach(f -> this.FUNCTIONS.put(f.getName(), f));
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.MAPPER = objectMapper;
    }

}
