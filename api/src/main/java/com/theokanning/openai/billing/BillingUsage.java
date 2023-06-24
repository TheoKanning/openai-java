package com.theokanning.openai.billing;

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
     * 账号金额消耗明细
     */
    @JsonProperty("daily_costs")
    private List<DailyCost> dailyCosts;
    /**
     * 总使用金额：美分
     */
    @JsonProperty("total_usage")
    private BigDecimal totalUsage;

}
