package com.theokanning.openai.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class ChatFunctionCallMixIn {

    @JsonSerialize(using = ChatFunctionCallArgumentsSerializerAndDeserializer.Serializer.class)
    @JsonDeserialize(using = ChatFunctionCallArgumentsSerializerAndDeserializer.Deserializer.class)
    abstract ObjectNode getArguments();

}
