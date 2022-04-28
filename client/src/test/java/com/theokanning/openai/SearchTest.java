package com.theokanning.openai;

import com.theokanning.openai.search.SearchRequest;
import com.theokanning.openai.search.SearchResult;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertFalse;

public class SearchTest {

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void search() {
        SearchRequest searchRequest = SearchRequest.builder()
                .documents(Arrays.asList("Water", "Earth", "Electricity", "Fire"))
                .query("Pikachu")
                .build();

        List<SearchResult> results = service.search("ada", searchRequest);
        assertFalse(results.isEmpty());
    }
}
