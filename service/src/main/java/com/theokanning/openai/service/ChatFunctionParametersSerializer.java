package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kjetland.jackson.jsonSchema.JsonSchemaConfig;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

import java.io.IOException;

public class ChatFunctionParametersSerializer extends JsonSerializer<Object> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonSchemaConfig config = JsonSchemaConfig.vanillaJsonSchemaDraft4();
    private final JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(mapper, config);

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            try {
                JsonNode schema = jsonSchemaGenerator.generateJsonSchema(value.getClass());
                gen.writeObject(schema);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate JSON Schema", e);
            }
        }
    }
}





