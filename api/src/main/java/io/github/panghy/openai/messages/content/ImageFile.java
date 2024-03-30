package io.github.panghy.openai.messages.content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * References an image File int eh content of a message.
 * <p>
 * /https://platform.openai.com/docs/api-reference/messages/object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageFile {

    /**
     * The File ID of the image in the message content.
     */
    @JsonProperty("file_id")
    String fileId;
}
