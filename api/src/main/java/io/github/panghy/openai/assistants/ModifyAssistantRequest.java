package io.github.panghy.openai.assistants;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModifyAssistantRequest {

    /**
     * ID of the model to use
     */
    String model;

    /**
     * The name of the assistant. The maximum length is 256
     */
    String name;

    /**
     * The description of the assistant.
     */
    String description;

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
