package com.theokanning.openai.runs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 22:34
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCallCodeInterpreter {
    
    private String input;
    
    private List<ToolCallCodeInterpreterOutput> outputs;
}
