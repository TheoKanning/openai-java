package com.theokanning.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Runs {

    String object;
    List<Runs> data;
    @JsonProperty("first_id")
    String firstId;
    @JsonProperty("last_id")
    String lastId;
    @JsonProperty("has_more")
    boolean hasMore;
}
