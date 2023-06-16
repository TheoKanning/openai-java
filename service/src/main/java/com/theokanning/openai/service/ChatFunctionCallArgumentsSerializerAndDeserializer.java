package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class ChatFunctionCallArgumentsSerializerAndDeserializer {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private ChatFunctionCallArgumentsSerializerAndDeserializer() {
    }

    public static class Serializer extends JsonSerializer<ObjectNode> {

        private Serializer() {
        }

        @Override
        public void serialize(ObjectNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(value.toString());
            }
        }
    }

    public static class Deserializer extends JsonDeserializer<ObjectNode> {

        private Deserializer() {
        }

        @Override
        public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String json = p.getValueAsString();
            if (json == null) {
                return null;
            } else {
                return (ObjectNode) MAPPER.readTree(json);
            }
        }
    }

}
