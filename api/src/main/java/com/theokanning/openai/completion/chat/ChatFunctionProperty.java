package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
@Builder
public class ChatFunctionProperty {
    @NonNull
    @JsonIgnore
    private String name;
    @NonNull
    private String type;
    @JsonIgnore
    private Boolean required;
    private String description;
    private ChatFunctionProperty items;
    @JsonProperty("enum")
    private Set<?> enumValues;
}