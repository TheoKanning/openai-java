package com.theokanning.openai.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.theokanning.openai.completion.chat.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ChatCompletionTest {

    static class Weather {
        @JsonPropertyDescription("City and state, for example: León, Guanajuato")
        public String location;

        @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
        @JsonProperty(required = true)
        public WeatherUnit unit;
    }

    enum WeatherUnit {
        CELSIUS, FAHRENHEIT;
    }

    static class WeatherResponse {
        public String location;
        public WeatherUnit unit;
        public int temperature;
        public String description;

        public WeatherResponse(String location, WeatherUnit unit, int temperature, String description) {
            this.location = location;
            this.unit = unit;
            this.temperature = temperature;
            this.description = description;
        }
    }

    String token = System.getenv("OPENAI_TOKEN");
    OpenAiService service = new OpenAiService(token);

    @Test
    void createChatCompletion() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(5)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .build();

        List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        assertEquals(5, choices.size());
    }

    @Test
    void streamChatCompletion() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a dog and will speak as such.");
        messages.add(systemMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .n(1)
                .maxTokens(50)
                .logitBias(new HashMap<>())
                .stream(true)
                .build();

        List<ChatCompletionChunk> chunks = new ArrayList<>();
        service.streamChatCompletion(chatCompletionRequest).blockingForEach(chunks::add);
        assertTrue(chunks.size() > 0);
        assertNotNull(chunks.get(0).getChoices().get(0));
    }

    @Test
    void createChatCompletionWithFunctions() {
        final List<ChatFunction> functions = Collections.singletonList(ChatFunction.builder()
                .name("get_weather")
                .description("Get the current weather in a given location")
                .executor(Weather.class, w -> new WeatherResponse(w.location, w.unit, 25, "sunny"))
                .build());
        final FunctionExecutor functionExecutor = new FunctionExecutor(functions);

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful assistant.");
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "What is the weather in Monterrey, Nuevo León?");
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();

        ChatCompletionChoice choice = service.createChatCompletion(chatCompletionRequest).getChoices().get(0);
        assertEquals("function_call", choice.getFinishReason());
        assertNotNull(choice.getMessage().getFunctionCall());
        assertEquals("get_weather", choice.getMessage().getFunctionCall().getName());
        assertInstanceOf(ObjectNode.class, choice.getMessage().getFunctionCall().getArguments());

        ChatMessage callResponse = functionExecutor.executeAndConvertToMessageHandlingExceptions(choice.getMessage().getFunctionCall());
        assertNotEquals("error", callResponse.getName());

        // this performs an unchecked cast
        WeatherResponse functionExecutionResponse = functionExecutor.execute(choice.getMessage().getFunctionCall());
        assertInstanceOf(WeatherResponse.class, functionExecutionResponse);
        assertEquals(25, functionExecutionResponse.temperature);

        JsonNode jsonFunctionExecutionResponse = functionExecutor.executeAndConvertToJson(choice.getMessage().getFunctionCall());
        assertInstanceOf(ObjectNode.class, jsonFunctionExecutionResponse);
        assertEquals("25", jsonFunctionExecutionResponse.get("temperature").asText());

        messages.add(choice.getMessage());
        messages.add(callResponse);

        ChatCompletionRequest chatCompletionRequest2 = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();

        ChatCompletionChoice choice2 = service.createChatCompletion(chatCompletionRequest2).getChoices().get(0);
        assertNotEquals("function_call", choice2.getFinishReason()); // could be stop or length, but should not be function_call
        assertNull(choice2.getMessage().getFunctionCall());
        assertNotNull(choice2.getMessage().getContent());
    }

    @Test
    void createChatCompletionWithDynamicFunctions() {
        ChatFunctionDynamic function = ChatFunctionDynamic.builder()
                .name("get_weather")
                .description("Get the current weather of a location")
                .addProperty(ChatFunctionProperty.builder()
                        .name("location")
                        .type("string")
                        .description("City and state, for example: León, Guanajuato")
                        .build())
                .addProperty(ChatFunctionProperty.builder()
                        .name("unit")
                        .type("string")
                        .description("The temperature unit, can be 'celsius' or 'fahrenheit'")
                        .enumValues(new HashSet<>(Arrays.asList("celsius", "fahrenheit")))
                        .required(true)
                        .build())
                .build();

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful assistant.");
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "What is the weather in Monterrey, Nuevo León?");
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(Collections.singletonList(function))
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();

        ChatCompletionChoice choice = service.createChatCompletion(chatCompletionRequest).getChoices().get(0);
        assertEquals("function_call", choice.getFinishReason());
        assertNotNull(choice.getMessage().getFunctionCall());
        assertEquals("get_weather", choice.getMessage().getFunctionCall().getName());
        assertInstanceOf(ObjectNode.class, choice.getMessage().getFunctionCall().getArguments());
        assertNotNull(choice.getMessage().getFunctionCall().getArguments().get("location"));
        assertNotNull(choice.getMessage().getFunctionCall().getArguments().get("unit"));
    }

    @Test
    void streamChatCompletionWithFunctions() {
        final List<ChatFunction> functions = Collections.singletonList(ChatFunction.builder()
                .name("get_weather")
                .description("Get the current weather in a given location")
                .executor(Weather.class, w -> new WeatherResponse(w.location, w.unit, 25, "sunny"))
                .build());
        final FunctionExecutor functionExecutor = new FunctionExecutor(functions);

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful assistant.");
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "What is the weather in Monterrey, Nuevo León?");
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();

        ChatMessage accumulatedMessage = service.mapStreamToAccumulator(service.streamChatCompletion(chatCompletionRequest))
                .blockingLast()
                .getAccumulatedMessage();
        assertNotNull(accumulatedMessage.getFunctionCall());
        assertEquals("get_weather", accumulatedMessage.getFunctionCall().getName());
        assertInstanceOf(ObjectNode.class, accumulatedMessage.getFunctionCall().getArguments());

        ChatMessage callResponse = functionExecutor.executeAndConvertToMessageHandlingExceptions(accumulatedMessage.getFunctionCall());
        assertNotEquals("error", callResponse.getName());

        // this performs an unchecked cast
        WeatherResponse functionExecutionResponse = functionExecutor.execute(accumulatedMessage.getFunctionCall());
        assertInstanceOf(WeatherResponse.class, functionExecutionResponse);
        assertEquals(25, functionExecutionResponse.temperature);

        JsonNode jsonFunctionExecutionResponse = functionExecutor.executeAndConvertToJson(accumulatedMessage.getFunctionCall());
        assertInstanceOf(ObjectNode.class, jsonFunctionExecutionResponse);
        assertEquals("25", jsonFunctionExecutionResponse.get("temperature").asText());


        messages.add(accumulatedMessage);
        messages.add(callResponse);

        ChatCompletionRequest chatCompletionRequest2 = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();

        ChatMessage accumulatedMessage2 = service.mapStreamToAccumulator(service.streamChatCompletion(chatCompletionRequest2))
                .blockingLast()
                .getAccumulatedMessage();
        assertNull(accumulatedMessage2.getFunctionCall());
        assertNotNull(accumulatedMessage2.getContent());
    }

    @Test
    void streamChatCompletionWithDynamicFunctions() {
        ChatFunctionDynamic function = ChatFunctionDynamic.builder()
                .name("get_weather")
                .description("Get the current weather of a location")
                .addProperty(ChatFunctionProperty.builder()
                        .name("location")
                        .type("string")
                        .description("City and state, for example: León, Guanajuato")
                        .build())
                .addProperty(ChatFunctionProperty.builder()
                        .name("unit")
                        .type("string")
                        .description("The temperature unit, can be 'celsius' or 'fahrenheit'")
                        .enumValues(new HashSet<>(Arrays.asList("celsius", "fahrenheit")))
                        .required(true)
                        .build())
                .build();

        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful assistant.");
        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "What is the weather in Monterrey, Nuevo León?");
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .functions(Collections.singletonList(function))
                .n(1)
                .maxTokens(100)
                .logitBias(new HashMap<>())
                .build();

        ChatMessage accumulatedMessage = service.mapStreamToAccumulator(service.streamChatCompletion(chatCompletionRequest))
                .blockingLast()
                .getAccumulatedMessage();
        assertNotNull(accumulatedMessage.getFunctionCall());
        assertEquals("get_weather", accumulatedMessage.getFunctionCall().getName());
        assertInstanceOf(ObjectNode.class, accumulatedMessage.getFunctionCall().getArguments());
        assertNotNull(accumulatedMessage.getFunctionCall().getArguments().get("location"));
        assertNotNull(accumulatedMessage.getFunctionCall().getArguments().get("unit"));
    }

    @Test
    void createChatCompletionWithImageInput() {
        final List<ChatMessage> messages = new ArrayList<>();
        List<ChatMessageContent> content = new ArrayList<>();
        content.add(new ChatMessageContent("What’s in this image?"));
        content.add(new ChatMessageContent(new ImageUrl(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg")));
        messages.add(new ChatMessage<>(ChatMessageRole.USER.value(), content));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
            .builder()
            .model("gpt-4-vision-preview")
            .messages(messages)
            .maxTokens(300)
            .build();

        List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        assertFalse(choices.isEmpty());
    }

    @Test
    void streamChatCompletionWithImageInput() {
        final List<ChatMessage> messages = new ArrayList<>();
        List<ChatMessageContent> content = new ArrayList<>();
        content.add(new ChatMessageContent("What’s in this image?"));
        content.add(new ChatMessageContent(new ImageUrl(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg")));
        messages.add(new ChatMessage<>(ChatMessageRole.USER.value(), content));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
            .builder()
            .model("gpt-4-vision-preview")
            .messages(messages)
            .maxTokens(300)
            .build();

        List<ChatCompletionChunk> chunks = new ArrayList<>();
        service.streamChatCompletion(chatCompletionRequest).blockingForEach(chunks::add);
        assertFalse(chunks.isEmpty());
        assertNotNull(chunks.get(0).getChoices().get(0));
    }

}
