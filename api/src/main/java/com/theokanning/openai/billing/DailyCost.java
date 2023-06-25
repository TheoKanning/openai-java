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
     *
     */
    @JsonProperty("timestamp")
    private long timestamp;
    /**
     * Model consumption amount details
     */
    @JsonProperty("line_items")
    private List<LineItem> lineItems;
}
