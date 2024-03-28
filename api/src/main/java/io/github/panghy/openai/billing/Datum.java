package io.github.panghy.openai.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 *
 */
@Data
public class Datum {
    private String object;
    private String id;
    /**
     * Gift amount: US dollars
     */
    @JsonProperty("grant_amount")
    private BigDecimal grantAmount;
    /**
     * Usage amount: US dollars
     */
    @JsonProperty("used_amount")
    private BigDecimal usedAmount;
    /**
     * Effective timestamp
     */
    @JsonProperty("effective_at")
    private Long effectiveAt;
    /**
     * Expiration timestamp
     */
    @JsonProperty("expires_at")
    private Long expiresAt;
}
