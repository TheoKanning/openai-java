package com.theokanning.openai.messages.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The text content that is part of a message
 * <p>
 * https://platform.openai.com/docs/api-reference/messages/object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Text {

    /**
     * The data that makes up the text.
     */
    String value;

    /**
     * Text annotations that show additional details
     */
    List<Annotation> annotations;
}
