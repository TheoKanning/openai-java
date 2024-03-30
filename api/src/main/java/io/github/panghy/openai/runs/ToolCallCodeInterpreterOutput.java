package io.github.panghy.openai.runs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 22:34
 **/


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCallCodeInterpreterOutput {
    
    private String type;
    
    private String logs;
    
    private RunImage image;
}
