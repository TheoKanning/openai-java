package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;

import java.io.IOException;

public class ChatCompletionRequestSerializerAndDeserializer {

    public static class Serializer extends JsonSerializer<ChatCompletionRequest.ChatCompletionRequestFunctionCall> {
        @Override
        public void serialize(ChatCompletionRequest.ChatCompletionRequestFunctionCall value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if ("none".equals(value.name) || "auto".equals(value.name)) {
                gen.writeString(value.name);
            } else {
                gen.writeStartObject();
                gen.writeFieldName("name");
                gen.writeString(value.name);
                gen.writeEndObject();
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<ChatCompletionRequest.ChatCompletionRequestFunctionCall> {
        @Override
        public ChatCompletionRequest.ChatCompletionRequestFunctionCall deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            if (p.getCurrentToken().isStructStart()) {
                p.nextToken(); //key
                p.nextToken(); //value
                return new ChatCompletionRequest.ChatCompletionRequestFunctionCall(p.getValueAsString());
            } else {
                return new ChatCompletionRequest.ChatCompletionRequestFunctionCall(p.getValueAsString());
            }

        }
    }
}
