package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * <p>Chat Message specialization for tool system
 * </p>
 *
 * see here for more info <a href="https://platform.openai.com/docs/guides/function-calling">Function Calling</a>
 */

@Data
public class ChatMessageTool extends ChatMessage {

    @JsonProperty("tool_call_id")
    private String toolCallId;

    public ChatMessageTool(String toolCallId, String role, String content, String name) {
        super(role,content,name);
        this.toolCallId = toolCallId;
    }
}
