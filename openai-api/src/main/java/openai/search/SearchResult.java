package openai.search;

import lombok.Data;

@Data
public class SearchResult {
    Integer document;
    String object;
    Double score;
}
