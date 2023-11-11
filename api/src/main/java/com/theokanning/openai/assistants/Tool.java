package com.theokanning.openai.assistants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tool {
     AssistantToolsEnum type;
}
