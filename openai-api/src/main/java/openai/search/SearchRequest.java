package openai.search;

import lombok.Data;

import java.util.List;

/**
 * A request to the document search api.
 * GPT-3 will perform a semantic search over the documents and score them based on how related they are to the query.
 * Higher scores indicate a stronger relation.
 *
 * https://beta.openai.com/docs/api-reference/search
 */
@Data
public class SearchRequest {
    /**
     * Documents to search over
     */
    List<String> documents;

    /**
     * Search query
     */
    String query;
}
