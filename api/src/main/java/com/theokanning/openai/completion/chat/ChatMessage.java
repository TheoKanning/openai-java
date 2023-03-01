package com.theokanning.openai.completion.chat;

import lombok.Builder;
import lombok.Data;

/**
 * <p>Each object has a role (either “system”, “user”, or “assistant”) and content (the content of the message). Conversations can be as short as 1 message or fill many pages.</p>
 * <p>Typically, a conversation is formatted with a system message first, followed by alternating user and assistant messages.</p>
 * <p>The system message helps set the behavior of the assistant. In the example above, the assistant was instructed with “You are a helpful assistant.”<br>
 * The user messages help instruct the assistant. They can be generated by the end users of an application, or set by a developer as an instruction.<br>
 * The assistant messages help store prior responses. They can also be written by a developer to help give examples of desired behavior.
 * </p>
 *
 * see <a href="https://platform.openai.com/docs/guides/chat/introduction">OpenAi documentation</a>
 */
@Data
@Builder
public class ChatMessage {

	ChatMessageRole role;
	String content;
}
