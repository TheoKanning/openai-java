package com.theokanning.openai.billing;

import lombok.Data;

import java.math.BigDecimal;

/**
 * List of amount consumption
 *
 */
@Data
public class LineItem {
    /**
     * 模型名称
     */
    private String name;
    /**
     * 消耗金额
     */
    private BigDecimal cost;
}
