package com.theokanning.openai.batch;

import lombok.Data;

/**
 * @Author: acone.wu
 * @date: 2024/5/15 13:50
 */
@Data
public class BatchError {

    /**
     * An error code identifying the error type.
     */
    String code;

    /**
     * The line number of the input file where the error occurred, if applicable
     */
    Integer line;

    /**
     * A human-readable message providing more details about the error.
     */
    String message;

    /**
     * The name of the parameter that caused the error, if applicable.
     */
    String param;
}
