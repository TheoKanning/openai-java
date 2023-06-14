package com.theokanning.openai.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

public abstract class ChatFunctionCallMixIn {

    @JsonSerialize(using = ChatFunctionCallArgumentsSerializerAndDeserializer.Serializer.class)
    @JsonDeserialize(using = ChatFunctionCallArgumentsSerializerAndDeserializer.Deserializer.class)
    abstract Map<String, Object> getArguments();

}
