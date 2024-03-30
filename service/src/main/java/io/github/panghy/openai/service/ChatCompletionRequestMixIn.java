package io.github.panghy.openai.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.panghy.openai.completion.chat.ChatCompletionRequest;

public abstract class ChatCompletionRequestMixIn {

    @JsonSerialize(using = ChatCompletionRequestSerializerAndDeserializer.Serializer.class)
    @JsonDeserialize(using = ChatCompletionRequestSerializerAndDeserializer.Deserializer.class)
    abstract ChatCompletionRequest.ChatCompletionRequestFunctionCall getFunctionCall();

}
