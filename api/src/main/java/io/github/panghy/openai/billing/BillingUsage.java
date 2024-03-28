package io.github.panghy.openai.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Amount consumption information
 *
 */
@Data
public class BillingUsage {

    @JsonProperty("object")
    private String object;
    /**
     * Account expenditure details
     */
    @JsonProperty("daily_costs")
    private List<DailyCost> dailyCosts;
    /**
     * Total usage amount: cents
     */
    @JsonProperty("total_usage")
    private BigDecimal totalUsage;

}
