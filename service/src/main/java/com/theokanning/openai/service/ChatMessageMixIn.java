package com.theokanning.openai.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author cong
 * @since 2023/11/17
 */
public abstract class ChatMessageMixIn {
    @JsonProperty("content")
    @JsonSerialize(using = ChatMessageSerializerAndDeserializer.ChatMessageContentSerializer.class)
    @JsonDeserialize(using = ChatMessageSerializerAndDeserializer.ChatMessageContentDeserializer.class)
    abstract Object getContent();
}
