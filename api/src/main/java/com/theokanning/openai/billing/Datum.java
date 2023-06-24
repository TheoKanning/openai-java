package com.theokanning.openai.billing;

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
     * 赠送金额：美元
     */
    @JsonProperty("grant_amount")
    private BigDecimal grantAmount;
    /**
     * 使用金额：美元
     */
    @JsonProperty("used_amount")
    private BigDecimal usedAmount;
    /**
     * 生效时间戳
     */
    @JsonProperty("effective_at")
    private Long effectiveAt;
    /**
     * 过期时间戳
     */
    @JsonProperty("expires_at")
    private Long expiresAt;
}
