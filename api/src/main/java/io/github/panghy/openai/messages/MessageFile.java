package io.github.panghy.openai.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * A list of files attached to a Message
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/file-object
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Jacksonized
public class MessageFile {
    /**
     * The identifier, which can be referenced in API endpoints.
     */
    String id;

    /**
     * The object type, which is always thread.message.file.
     */
    String object;

    /**
     * The Unix timestamp (in seconds) for when the message file was created.
     */
    @JsonProperty("created_at")
    int createdAt;

    /**
     * The ID of the message that the File is attached to.
     */
    @JsonProperty("message_id")
    String messageId;
}
