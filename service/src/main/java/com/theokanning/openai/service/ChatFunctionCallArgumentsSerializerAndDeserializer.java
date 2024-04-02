package com.theokanning.openai.service;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class ChatFunctionCallArgumentsSerializerAndDeserializer {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ChatFunctionCallArgumentsSerializerAndDeserializer() {
    }

    public static class Serializer extends JsonSerializer<JsonNode> {
        private Serializer() {
        }

        @Override
        public void serialize(JsonNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(value instanceof TextNode ? value.asText() : value.toPrettyString());
            }
        }
    }

    public abstract static class JsonNodeHandler {
        public abstract JsonNode handle(JsonParser p, DeserializationContext ctxt) throws IOException;
    }

    public static class MissingNodeHandler extends JsonNodeHandler {
        @Override
        public JsonNode handle(JsonParser p, DeserializationContext ctxt) {
            return JsonNodeFactory.instance.missingNode();
        }
    }

    public static class DefaultNodeHandler extends JsonNodeHandler {
        @Override
        public JsonNode handle(JsonParser p, DeserializationContext ctxt) throws IOException {
            return MAPPER.readTree(p);
        }
    }

    public static class Deserializer extends JsonDeserializer<JsonNode> {
        private static final Map<JsonToken, JsonNodeHandler> HANDLERS = initializeHandlers();

        private static Map<JsonToken, JsonNodeHandler> initializeHandlers() {
            Map<JsonToken, JsonNodeHandler> handlers = new HashMap<>();
            handlers.put(JsonToken.VALUE_NULL, new MissingNodeHandler());
            // Add more handlers for different token types if needed
            return handlers;
        }

        @Override
        public JsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = p.getCurrentToken();
            JsonNodeHandler handler = HANDLERS.getOrDefault(currentToken, new DefaultNodeHandler());
            return handler.handle(p, ctxt);
        }
    }

}