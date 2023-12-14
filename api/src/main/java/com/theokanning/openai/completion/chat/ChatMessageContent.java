package com.theokanning.openai.completion.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageContent {

    /**
     * The type of the content part
     *
     * @see ChatMessageContentType
     */
    private String type;

    /**
     * The text content.
     */
    private String text;

    /**
     * Image input is only supported when using the gpt-4-visual-preview model.
     */
    @JsonProperty("image_url")
    private ImageUrl imageUrl;

    public ChatMessageContent(String text) {
        this.type = ChatMessageContentType.TEXT.value();
        this.text = text;
    }

    public ChatMessageContent(ImageUrl imageUrl) {
        this.type = ChatMessageContentType.IMAGE_URL.value();
        this.imageUrl = imageUrl;
    }
}