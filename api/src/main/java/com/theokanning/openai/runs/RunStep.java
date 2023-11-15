package com.theokanning.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RunStep {

    @JsonProperty("assistant_id")
    String assistantId;
    @JsonProperty("canelled_at")
    Long cancelledAt;
    @JsonProperty("completed_at")
    Long completedAt;
    @JsonProperty("created_at")
    Long createdAt;
    @JsonProperty("expired_at")
    Long expiredAt;
    @JsonProperty("failed_at")
    Long failedAt;
    String id;
    @JsonProperty("last_error")
    String lastError;
    String object;
    @JsonProperty("run_id")
    String runId;
    String status;
    @JsonProperty("step_details")
    StepDetails stepDetails;
    @JsonProperty("thread_id")
    String threadId;
    String type;
}
