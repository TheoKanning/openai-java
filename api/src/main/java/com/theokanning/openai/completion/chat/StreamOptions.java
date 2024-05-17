package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Options for streaming response. Only set this when you set stream: true</p>
 * see <a href="https://platform.openai.com/docs/api-reference/chat/create#chat-create-stream_options">OpenAi documentation</a>
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class StreamOptions {

	/**
	 * If set, an additional chunk will be streamed before the data: [DONE] message.
	 * The usage field on this chunk shows the token usage statistics for the entire request, and the choices field will always be an empty array.
	 * All other chunks will also include a usage field, but with a null value.
	 */
	@JsonProperty("include_usage")
	Boolean includeUsage;

}
