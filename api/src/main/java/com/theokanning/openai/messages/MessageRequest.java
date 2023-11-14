package com.theokanning.openai.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Creates a Message
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/createMessage
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageRequest {
    /**
     * The role of the entity that is creating the message.
     * Currently only "user" is supported.
     */
    @NonNull
    @Builder.Default
    String role = "user";

    /**
     * The content of the message.
     */
    @NonNull
    String content;

    /**
     * A list of File IDs that the message should use.
     * Defaults to an empty list.
     * There can be a maximum of 10 files attached to a message.
     * Useful for tools like retrieval and code_interpreter that can access and use files.
     */
    @JsonProperty("file_ids")
    List<String> fileIds;

    /**
     * Set of 16 key-value pairs that can be attached to an object.
     * This can be useful for storing additional information about the object in a structured format.
     * Keys can be a maximum of 64 characters long, and values can be a maximum of 512 characters long.
     */
    Map<String, String> metadata;
}
