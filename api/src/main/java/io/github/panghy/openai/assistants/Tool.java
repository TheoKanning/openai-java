package io.github.panghy.openai.assistants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Tool {
     /**
      * The type of tool being defined
      */
     AssistantToolsEnum type;

     /**
      * Function definition, only used if type is "function"
      */
     AssistantFunction function;
}
