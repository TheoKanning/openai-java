package com.theokanning.openai.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * List of amount consumption
 *
 */
@Data
public class DailyCost {
    /**
     * 时间戳
     */
    @JsonProperty("timestamp")
    private long timestamp;
    /**
     * 模型消耗金额详情
     */
    @JsonProperty("line_items")
    private List<LineItem> lineItems;
}
