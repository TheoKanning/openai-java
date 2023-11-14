package com.theokanning.openai.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Modifies a Message
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/modifyMessage
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModifyMessageRequest {

    /**
     * Set of 16 key-value pairs that can be attached to an object.
     * This can be useful for storing additional information about the object in a structured format.
     * Keys can be a maximum of 64 characters long, and values can be a maximum of 512 characters long.
     */
    Map<String, String> metadata;
}
