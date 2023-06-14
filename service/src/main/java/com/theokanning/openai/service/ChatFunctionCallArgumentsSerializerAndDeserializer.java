package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.Map;

public class ChatFunctionCallArgumentsSerializerAndDeserializer {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private ChatFunctionCallArgumentsSerializerAndDeserializer() {
    }

    public static class Serializer extends JsonSerializer<Map<String, Object>> {

        private Serializer() {
        }

        @Override
        public void serialize(Map<String, Object> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                String json = MAPPER.writeValueAsString(value);
                gen.writeString(json);
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<Map<String, Object>> {

        private Deserializer() {
        }

        @Override
        public Map<String, Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String json = p.getValueAsString();
            if (json == null) {
                return null;
            } else {
                return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
                });
            }
        }
    }

}
