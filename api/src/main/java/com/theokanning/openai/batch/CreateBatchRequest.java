package com.theokanning.openai.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * @Author: acone.wu
 * @date: 2024/5/14 21:11
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateBatchRequest {

    /**
     * The ID of an uploaded file that contains requests for the new batch.
     * <p>
     * See upload file for how to upload a file.
     * <p>
     * Your input file must be formatted as a JSONL file, and must be uploaded with the purpose batch. The file can contain up to 50,000 requests, and can be up to 100 MB in size.
     */
    @NonNull
    @JsonProperty("input_file_id")
    String inputFileId;

    /**
     * The endpoint to be used for all requests in the batch. Currently /v1/chat/completions, /v1/embeddings, and /v1/completions are supported. Note that /v1/embeddings batches are also restricted to a maximum of 50,000 embedding inputs across all requests in the batch.
     */
    @NonNull
    String endpoint;

    /**
     * The time frame within which the batch should be processed. Currently only 24h is supported.
     */
    @NonNull
    @JsonProperty("completion_window")
    String compWindow = "24h";

    /**
     * Optional custom metadata for the batch.
     */
    Map<String, Object> metadata;
}
