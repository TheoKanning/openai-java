package com.theokanning.openai.completion.chat;

import lombok.Data;
import lombok.NonNull;


@Data
public class ChatFunctionDynamic {

    /**
     * The name of the function being called.
     */
    @NonNull
    private String name;

    /**
     * A description of what the function does, used by the model to choose when and how to call the function.
     */
    private String description;

    /**
     * The parameters the functions accepts.
     */
    private ChatFunctionParameters parameters;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private ChatFunctionParameters parameters = new ChatFunctionParameters();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder parameters(ChatFunctionParameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder addProperty(ChatFunctionProperty property) {
            this.parameters.addProperty(property);
            return this;
        }

        public ChatFunctionDynamic build() {
            ChatFunctionDynamic chatFunction = new ChatFunctionDynamic(name);
            chatFunction.setDescription(description);
            chatFunction.setParameters(parameters);
            return chatFunction;
        }
    }
}
