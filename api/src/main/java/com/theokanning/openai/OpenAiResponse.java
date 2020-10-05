package com.theokanning.openai;

import lombok.Data;

import java.util.List;

/**
 * A wrapper class to fit the OpenAI engine and search endpoints
 */
@Data
public class OpenAiResponse<T> {
    /**
     * A list containing the actual results
     */
    public List<T> data;

    /**
     * The type of object returned, should be "list"
     */
    public String object;
}
