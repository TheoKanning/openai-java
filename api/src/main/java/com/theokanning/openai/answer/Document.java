package com.theokanning.openai.answer;

import lombok.Data;

/**
 * Represents an example returned by the classification api
 *
 * https://beta.openai.com/docs/api-reference/classifications/create
 */
@Deprecated
@Data
public class Document {
    /**
     * The position of this example in the example list
     */
    Integer document;

    /**
     * The text of the example
     */
    String text;
}
