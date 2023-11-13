package com.theokanning.openai.assistants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AssistantFile {

    /**
     * The identifier of the Assistant File
     */
    String id;

    /**
     * The object type, which is always assistant.file.
     */
    String object;

    /**
     * The Unix timestamp (in seconds) for when the assistant file was created.
     */
    @JsonProperty("created_at")
    String createdAt;

    /**
     * The assistant ID that the file is attached to
     */
    @JsonProperty("assistant_id")
    String assistantId;
}
