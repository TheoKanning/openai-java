package io.github.panghy.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 22:32
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCall {
    
    private String id;
    
    private String type;
    
    @JsonProperty("code_interpreter")
    private ToolCallCodeInterpreter codeInterpreter;

    private Map<String, String> retrieval;

    private ToolCallFunction function;
}
