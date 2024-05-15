package com.theokanning.openai.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: acone.wu
 * @date: 2024/5/15 16:07
 */
@Data
public class BatchListResult {

    private String object;

    private List<Batch> data;

    @JsonProperty("first_id")
    private String firstId;

    @JsonProperty("last_id")
    private String lastId;

    @JsonProperty("has_more")
    private Boolean hasMore;

}
