package io.github.panghy.openai.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 22:45
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitToolOutputRequestItem {

    @JsonProperty("tool_call_id")
    private String toolCallId;
    
    private String output;
}
