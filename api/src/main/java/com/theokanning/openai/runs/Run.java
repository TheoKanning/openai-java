package com.theokanning.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Run {

    @JsonProperty("assistant_id")
    String assistantId;
    @JsonProperty("cancelled_at")
    Long cancelledAt;
    @JsonProperty("completed_at")
    Long completedAt;
    @JsonProperty("created_at")
    Long createdAt;
    @JsonProperty("expires_at")
    Long expiresAt;
    @JsonProperty("failed_at")
    Long failedAt;
    @JsonProperty("file_ids")
    List<String> fileIds;
    String id;
    String instructions;
    @JsonProperty("last_error")
    String lastError;
    Map<String, String> metadata;
    String model;
    String object;
    @JsonProperty("started_at")
    Long startedAt;
    String status;
    @JsonProperty("thread_id")
    String threadId;
    List<Tool> tools;
}
