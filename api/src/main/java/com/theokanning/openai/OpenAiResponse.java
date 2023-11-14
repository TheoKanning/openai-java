package com.theokanning.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * The first id included
     */
    @JsonProperty("first_id")
    public String firstId;

    /**
     * The last id included
     */
    @JsonProperty("last_id")
    public String lastId;

    /**
     * True if there are objects after lastId
     */
    @JsonProperty("hasMore")
    public boolean hasMore;
}
