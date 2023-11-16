package com.theokanning.openai.runs;

import com.theokanning.openai.assistants.Tool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RunCreateRequest {
    String assistantId;

    // Optional
    String model;
    
    String instructions;
    
    List<Tool> tools;
    
    Map<String, String> metadata;
}
