package io.github.panghy.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * A wrapper class to fit the OpenAI engine and search endpoints
 */
@Data
@Builder
@Jacksonized
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
    @JsonProperty("has_more")
    public boolean hasMore;
}
