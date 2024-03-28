package io.github.panghy.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.panghy.openai.assistants.Tool;
import io.github.panghy.openai.common.LastError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Run {

    private String id;

    private String object;

    @JsonProperty("created_at")
    private Integer createdAt;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("assistant_id")
    private String assistantId;

    private String status;

    @JsonProperty("required_action")
    private RequiredAction requiredAction;

    @JsonProperty("last_error")
    private LastError lastError;

    @JsonProperty("expires_at")
    private Integer expiresAt;

    @JsonProperty("started_at")
    private Integer startedAt;
    
    @JsonProperty("cancelled_at")
    private Integer cancelledAt;

    @JsonProperty("failed_at")
    private Integer failedAt;
    
    @JsonProperty("completed_at")
    private Integer completedAt;

    private String model;

    private String instructions;

    private List<Tool> tools;
    
    @JsonProperty("file_ids")
    private List<String> fileIds;

    private Map<String, String> metadata;
}
