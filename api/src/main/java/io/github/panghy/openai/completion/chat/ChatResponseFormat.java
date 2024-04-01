package io.github.panghy.openai.completion.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseFormat {

  /**
   * JSON mode guarantees the message the model generates is valid JSON.
   */
  public static final ChatResponseFormat JSON = ChatResponseFormat.builder().type("json_object").build();

  /**
   * Setting type to "json_object" enables JSON mode, which guarantees the message the model generates is valid JSON.
   */
  String type;
}
