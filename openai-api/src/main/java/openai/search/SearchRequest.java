package openai.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    List<String> documents;
    String query;
}
