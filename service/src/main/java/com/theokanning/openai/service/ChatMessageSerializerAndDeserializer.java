package com.theokanning.openai.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.theokanning.openai.completion.chat.ChatMessageContent;
import com.theokanning.openai.completion.chat.ChatMessageContentType;
import com.theokanning.openai.completion.chat.ImageUrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatMessageSerializerAndDeserializer {

    public static class ChatMessageContentSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object content, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (content == null) {
                gen.writeNull();
                return;
            }
            if (content instanceof String) {
                gen.writeString((String)content);
                return;
            }
            if (content instanceof List) {
                gen.writeStartArray();
                List<?> contentList = (List<?>)content;
                for (Object item : contentList) {
                    if (item instanceof ChatMessageContent) {
                        ChatMessageContent contentItem = (ChatMessageContent)item;
                        gen.writeStartObject();
                        gen.writeStringField("type", contentItem.getType());
                        if (ChatMessageContentType.TEXT.value().equals(contentItem.getType())) {
                            gen.writeStringField("text", contentItem.getText());
                        } else if (ChatMessageContentType.IMAGE_URL.value().equals(contentItem.getType())) {
                            gen.writeObjectFieldStart("image_url");
                            gen.writeStringField("url", contentItem.getImageUrl().getUrl());
                            gen.writeStringField("detail", contentItem.getImageUrl().getDetail());
                            gen.writeEndObject();
                        }
                        gen.writeEndObject();
                    }
                }
                gen.writeEndArray();
            }
        }
    }

    public static class ChatMessageContentDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode contentNode = p.readValueAsTree();
            if (contentNode.isTextual()) {
                return contentNode.asText();
            }
            if (contentNode.isArray()) {
                List<Object> contentList = new ArrayList<>();
                for (JsonNode itemNode : contentNode) {
                    String type = itemNode.get("type").asText();
                    if (ChatMessageContentType.TEXT.value().equals(type)) {
                        contentList.add(new ChatMessageContent(itemNode.get("text").asText()));
                    } else if (ChatMessageContentType.IMAGE_URL.value().equals(type)) {
                        JsonNode imageUrlJsonNode = itemNode.get("image_url");
                        ImageUrl imageUrl = new ImageUrl();
                        imageUrl.setUrl(Optional.ofNullable(imageUrlJsonNode.get("url"))
                            .map(JsonNode::asText).orElse(null));
                        imageUrl.setDetail(Optional.ofNullable(imageUrlJsonNode.get("detail"))
                            .map(JsonNode::asText).orElse(null));
                        contentList.add(new ChatMessageContent(imageUrl));
                    }
                }
                return contentList;
            }
            return null;
        }
    }

}
