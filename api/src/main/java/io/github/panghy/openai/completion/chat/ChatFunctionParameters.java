package io.github.panghy.openai.completion.chat;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ChatFunctionParameters {

    private final String type = "object";

    private final HashMap<String, ChatFunctionProperty> properties = new HashMap<>();

    private List<String> required;

    public void addProperty(ChatFunctionProperty property) {
        properties.put(property.getName(), property);
        if (Boolean.TRUE.equals(property.getRequired())) {
            if (this.required == null) {
                this.required = new ArrayList<>();
            }
            this.required.add(property.getName());
        }
    }
}
