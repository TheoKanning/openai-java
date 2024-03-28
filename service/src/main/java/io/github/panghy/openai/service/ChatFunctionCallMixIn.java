package io.github.panghy.openai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public abstract class ChatFunctionCallMixIn {

    @JsonSerialize(using = ChatFunctionCallArgumentsSerializerAndDeserializer.Serializer.class)
    @JsonDeserialize(using = ChatFunctionCallArgumentsSerializerAndDeserializer.Deserializer.class)
    abstract JsonNode getArguments();

}
