package io.github.panghy.openai.assistants;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Assistant {

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

    /**
     * The name of the assistant. The maximum length is 256
     */
    String name;

    /**
     * The description of the assistant.
     */
    String description;

    /**
     * ID of the model to use
     */
    @NonNull
    String model;

    /**
     * The system instructions that the assistant uses.
     */
    String instructions;

    /**
     * A list of tools enabled on the assistant.
     */
    List<Tool> tools;

    /**
     * A list of file IDs attached to this assistant.
     */
    @JsonProperty("file_ids")
    List<String> fileIds;

    /**
     * Set of 16 key-value pairs that can be attached to an object.
     */
    Map<String, String> metadata;
}
