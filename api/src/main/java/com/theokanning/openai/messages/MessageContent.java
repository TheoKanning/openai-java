package com.theokanning.openai.messages;

import lombok.Data;


/**
 * Represents the content of a message
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/object
 */
@Data
public class MessageContent {
    /**
     * The content type, either "text" or "image_file"
     */
    String type;

    // todo handle different content types
}
