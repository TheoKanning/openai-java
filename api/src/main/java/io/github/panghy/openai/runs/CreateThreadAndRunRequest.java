package io.github.panghy.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.panghy.openai.assistants.Tool;
import io.github.panghy.openai.threads.ThreadRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 23:08
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateThreadAndRunRequest {
    
    @JsonProperty("assistant_id")
    private String assistantId;
    
    private ThreadRequest thread;

    private String model;
    
    private String instructions;

    private List<Tool> tools;

    private Map<String, String> metadata;
}
