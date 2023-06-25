package com.theokanning.openai.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Return value of balance inquiry interface
 *
 */
@Data
public class CreditGrantsResponse implements Serializable {
    private String object;
    /**
     * Total amount: US dollars
     */
    @JsonProperty("total_granted")
    private BigDecimal totalGranted;
    /**
     * Total usage amount: US dollars
     */
    @JsonProperty("total_used")
    private BigDecimal totalUsed;
    /**
     * Total remaining amount: US dollars
     */
    @JsonProperty("total_available")
    private BigDecimal totalAvailable;
    /**
     * Balance details
     */
    private Grants grants;
}
