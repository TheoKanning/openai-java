package com.theokanning.openai.assistants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @description:
 * @author: vacuity
 * @create: 2023-11-20 10:09
 **/


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssistantFunction {

    private String description;
    
    private String name;
    
    private Map<String, Object> parameters;
}
