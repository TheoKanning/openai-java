package com.theokanning.openai.classification;

import lombok.Data;

/**
 * Represents an example returned by the classification api
 *
 * https://beta.openai.com/docs/api-reference/classifications/create
 */
@Deprecated
@Data
public class Example {
    /**
     * The position of this example in the example list
     */
    Integer document;

    /**
     * The label of the example
     */
    String label;

    /**
     * The text of the example
     */
    String text;
}
