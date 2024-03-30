package io.github.panghy.openai.runs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-16 22:38
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCallFunction {
    
    private String name;
    
    private String arguments;
    
    private String output;
}
