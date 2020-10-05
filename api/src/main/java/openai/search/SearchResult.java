package openai.search;

import lombok.Data;

/**
 * A search result for a single document.
 *
 * https://beta.openai.com/docs/api-reference/search
 */
@Data
public class SearchResult {
    /**
     * The position of this document in the request list
     */
    Integer document;

    /**
     * The type of object returned, should be "search_result"
     */
    String object;

    /**
     * A number measuring the document's correlation with the query.
     * A higher score means a stronger relationship.
     */
    Double score;
}
