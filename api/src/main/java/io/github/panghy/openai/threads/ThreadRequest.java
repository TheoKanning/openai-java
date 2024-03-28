package io.github.panghy.openai.threads;

import io.github.panghy.openai.messages.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Creates a thread
 * <p>
 * https://platform.openai.com/docs/api-reference/threads/createThread
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ThreadRequest {
    /**
     * A list of messages to start the thread with. Optional.
     */
    List<MessageRequest> messages;

    /**
     * Set of 16 key-value pairs that can be attached to an object.
     * This can be useful for storing additional information about the object in a structured format.
     * Keys can be a maximum of 64 characters long, and values can be a maximum of 512 characters long.
     */
    Map<String, String> metadata;
}
