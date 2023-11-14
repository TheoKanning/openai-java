package com.theokanning.openai.assistants;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.OpenAiResponse;

public class ListAssistant<T> extends OpenAiResponse<T> {

    @JsonProperty("first_id")
    String firstId;

    @JsonProperty("last_id")
    String lastId;

    @JsonProperty("has_more")
    boolean hasMore;
}
