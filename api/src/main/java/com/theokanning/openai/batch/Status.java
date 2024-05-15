package com.theokanning.openai.batch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: acone.wu
 * @date: 2024/5/15 15:40
 */
public enum Status {
    VALIDATING, FAILED, IN_PROGRESS, FINALIZING, COMPLETED, EXPIRED, CANCELLING, CANCELLED
    ;

    @JsonCreator
    public static Status fromValue(String value) {
        return Status.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
