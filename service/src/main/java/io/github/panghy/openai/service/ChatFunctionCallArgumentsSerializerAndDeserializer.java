package io.github.panghy.openai.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

public class ChatFunctionCallArgumentsSerializerAndDeserializer {

    private final static ObjectMapper MAPPER = new ObjectMapper();

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

    public static class Deserializer extends JsonDeserializer<JsonNode> {

        private Deserializer() {
        }

        @Override
        public JsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String json = p.getValueAsString();
            if (json == null || p.currentToken() == JsonToken.VALUE_NULL) {
                return null;
            }

            try {
                JsonNode node = null;
                try {
                    node = MAPPER.readTree(json);
                } catch (JsonParseException ignored) {
                }
                if (node == null || node.getNodeType() == JsonNodeType.MISSING) {
                    node = MAPPER.readTree(p);
                }
                return node;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

}
