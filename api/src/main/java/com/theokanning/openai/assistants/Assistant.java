package com.theokanning.openai.assistants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Assistant extends AssistantBase {

    /**
     * The identifier, which can be referenced in API endpoints.
     */
    String id;

    /**
     * The object type which is always 'assistant'
     */
    String object;

    /**
     * The Unix timestamp(in seconds) for when the assistant was created
     */
    @JsonProperty("created_at")
    Integer createdAt;
}
