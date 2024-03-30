package io.github.panghy.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepDetails {

    
    private String type;

    @JsonProperty("message_creation")
    private MessageCreation messageCreation;

    @JsonProperty("tool_calls")
    private List<ToolCall> toolCalls;
}
