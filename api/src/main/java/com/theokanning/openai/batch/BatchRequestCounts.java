package com.theokanning.openai.batch;

import lombok.Data;

/**
 * @Author: acone.wu
 * @date: 2024/5/15 15:36
 */
@Data
public class BatchRequestCounts {

    /**
     * Number of requests that have been completed successfully.
     * */
    private Integer completed;

    /**
     * Number of requests that have failed.
     * */
    private Integer failed;

    /**
     * Total number of requests in the batch.
     * */
    private Integer total;
}
