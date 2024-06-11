package com.theokanning.openai.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @Author: acone.wu
 * @date: 2024/5/15 13:51
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Batch {

    @NonNull
    private String id;

    /**
     * The time frame within which the batch should be processed.
     */
    @NonNull
    @JsonProperty("completion_window")
    private String completionWindow;

    /**
     * The Unix timestamp (in seconds) for when the batch was created.
     */
    @NonNull
    @JsonProperty("created_at")
    private Integer createdAt;

    /**
     * The OpenAI API endpoint used by the batch.
     */
    @NonNull
    private String endpoint;

    /**
     * The ID of the input file for the batch.
     */
    @NonNull
    @JsonProperty("input_file_id")
    private String inputFileId;

    /**
     * The object type, which is always `batch`.
     */
    private String object = "batch";

    /**
     * The current status of the batch.
     * */
    private Status status;

    /**
     * The Unix timestamp (in seconds) for when the batch was cancelled.
     * */
    @JsonProperty("cancelled_at")
    private Integer cancelledAt;

    /**
     * The Unix timestamp (in seconds) for when the batch started cancelling.
     * */
    @JsonProperty("cancelling_at")
    private Integer cancellingAt;

    /**
     * The Unix timestamp (in seconds) for when the batch was completed.
     * */
    @JsonProperty("completed_at")
    private Integer completedAt;

    /**
     * The ID of the file containing the outputs of requests with errors.
     * */
    @JsonProperty("error_file_id")
    private String errorFileId;

    /**
     * Errors associated with batch processing.
     * */
    private Errors errors;

    /**
     * The Unix timestamp (in seconds) for when the batch expired.
     * */
    @JsonProperty("expired_at")
    private Integer expiredAt;

    /**
     * The Unix timestamp (in seconds) for when the batch will expire.
     * */
    @JsonProperty("expires_at")
    private Integer expiresAt;

    /**
     * The Unix timestamp (in seconds) for when the batch failed.
     * */
    @JsonProperty("failed_at")
    private Integer failedAt;

    /**
     * The Unix timestamp (in seconds) for when the batch started finalizing.
     * */
    @JsonProperty("finalizing_at")
    private Integer finalizingAt;

    /**
     * The Unix timestamp (in seconds) for when the batch started processing.
     * */
    @JsonProperty("in_progress_at")
    private Integer inProgressAt;

    /**
     * Metadata for storing additional information about the object.
     * */
    private Map<String, Object> metadata;

    /**
     * The ID of the file containing the outputs of successfully executed requests.
     * */
    @JsonProperty("output_file_id")
    private String outputFileId;

    /**
     * The request counts for different statuses within the batch.
     * */
    @JsonProperty("request_counts")
    private BatchRequestCounts requestCounts;

    @Data
    public static class Errors {
        private List<BatchError> data;

        private String object;
    }
}
