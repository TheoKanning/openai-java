package io.github.panghy.openai.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.completion.chat.*;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatCompletionTest {

    static class Weather {
        @JsonPropertyDescription("City and state, for example: León, Guanajuato")
        public String location;

        @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
        @JsonProperty(required = true)
        public WeatherUnit unit;
    }

    enum WeatherUnit {
        CELSIUS, FAHRENHEIT
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

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

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

        when(mockApi.createChatCompletion(chatCompletionRequest)).thenReturn(just(ChatCompletionResult.builder()
            .choices(Arrays.asList(
                ChatCompletionChoice.builder()
                    .finishReason("length")
                    .message(ChatMessage.builder()
                        .role(ChatMessageRole.SYSTEM.value())
                        .content("You are a dog and will speak as such.")
                        .build())
                    .build(),
                ChatCompletionChoice.builder()
                    .finishReason("length")
                    .message(ChatMessage.builder()
                        .role(ChatMessageRole.USER.value())
                        .content("Woof woof!")
                        .build())
                    .build(),
                ChatCompletionChoice.builder()
                    .finishReason("length")
                    .message(ChatMessage.builder()
                        .role(ChatMessageRole.SYSTEM.value())
                        .content("You are a dog and will speak as such.")
                        .build())
                    .build(),
                ChatCompletionChoice.builder()
                    .finishReason("length")
                    .message(ChatMessage.builder()
                        .role(ChatMessageRole.USER.value())
                        .content("Woof woof!")
                        .build())
                    .build(),
                ChatCompletionChoice.builder()
                    .finishReason("length")
                    .message(ChatMessage.builder()
                        .role(ChatMessageRole.SYSTEM.value())
                        .content("You are a dog and will speak as such.")
                        .build())
                    .build()
            ))
            .build()));
        List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        verify(mockApi, times(1)).createChatCompletion(chatCompletionRequest);
        assertEquals(5, choices.size());
    }

    @Test
    void streamChatCompletion() {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),
            "You are a dog and will speak as such.");
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
        when(mockApi.createChatCompletionStream(chatCompletionRequest)).
            // fake SSE Response
                thenReturn(new FakeCall<>(ResponseBody.create(MediaType.parse("text/event-stream"),
                """
                    data: {"choices": [{"finish_reason": "length", "message": {"role": "system", "content": "You are a dog and will speak as such."}}]}

                    data: {"choices": [{"finish_reason": "length", "message": {"role": "user", "content": "Woof woof!"}}]}

                    """)));
        service.streamChatCompletion(chatCompletionRequest).blockingForEach(chunks::add);
        verify(mockApi, times(1)).createChatCompletionStream(chatCompletionRequest);
        assertFalse(chunks.isEmpty());
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

        when(mockApi.createChatCompletion(chatCompletionRequest)).thenReturn(just(ChatCompletionResult.builder()
            .choices(Collections.singletonList(ChatCompletionChoice.builder()
                .finishReason("function_call")
                .message(ChatMessage.builder()
                    .role(ChatMessageRole.FUNCTION.value())
                    .functionCall(ChatFunctionCall.builder()
                        .name("get_weather")
                        .arguments(new ObjectNode(JsonNodeFactory.instance))
                        .build())
                    .build())
                .build()))
            .build()));
        ChatCompletionChoice choice = service.createChatCompletion(chatCompletionRequest).getChoices().get(0);
        verify(mockApi, times(1)).createChatCompletion(chatCompletionRequest);
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

        when(mockApi.createChatCompletion(chatCompletionRequest2)).thenReturn(just(ChatCompletionResult.builder()
            .choices(Collections.singletonList(ChatCompletionChoice.builder()
                .finishReason("stop")
                .message(ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM.value())
                    .content("You are a helpful assistant.")
                    .build())
                .build()))
            .build()));
        ChatCompletionChoice choice2 = service.createChatCompletion(chatCompletionRequest2).getChoices().get(0);
        verify(mockApi, times(2)).createChatCompletion(chatCompletionRequest2);
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


        ChatCompletionResult result = ChatCompletionResult.builder()
            .choices(Collections.singletonList(ChatCompletionChoice.builder()
                .finishReason("function_call")
                .message(ChatMessage.builder()
                    .role(ChatMessageRole.FUNCTION.value())
                    .functionCall(ChatFunctionCall.builder()
                        .name("get_weather")
                        .arguments(JsonNodeFactory.instance.objectNode().
                            put("location", "Monterrey, Nuevo León").
                            put("unit", "CELSIUS"))
                        .build())
                    .build())
                .build())).build();

        when(mockApi.createChatCompletion(chatCompletionRequest)).thenReturn(just(result));
        ChatCompletionChoice choice = service.createChatCompletion(chatCompletionRequest).getChoices().get(0);
        assertEquals("function_call", choice.getFinishReason());
        assertNotNull(choice.getMessage().getFunctionCall());
        assertEquals("get_weather", choice.getMessage().getFunctionCall().getName());
        assertInstanceOf(ObjectNode.class, choice.getMessage().getFunctionCall().getArguments());
        assertNotNull(choice.getMessage().getFunctionCall().getArguments().get("location"));
        assertNotNull(choice.getMessage().getFunctionCall().getArguments().get("unit"));
    }

    @Test
    void streamChatCompletionWithFunctions() throws JsonProcessingException {
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

        ChatCompletionResult result = ChatCompletionResult.builder()
            .choices(Collections.singletonList(ChatCompletionChoice.builder()
                .finishReason("function_call")
                .message(ChatMessage.builder()
                    .role(ChatMessageRole.FUNCTION.value())
                    .functionCall(ChatFunctionCall.builder()
                        .name("get_weather")
                        .arguments(JsonNodeFactory.instance.textNode("{\"location\": \"Monterrey, Nuevo León\", \"unit\": \"CELSIUS\"}"))
                        .build())
                    .build())
                .build())).build();
        ObjectMapper om = new ObjectMapper();
        when(mockApi.createChatCompletionStream(chatCompletionRequest)).
            // fake SSE Response
                thenReturn(new FakeCall<>(ResponseBody.create(MediaType.parse("text/event-stream"),
                "data: " + om.writeValueAsString(result) + "\n\n")));
        ChatMessage accumulatedMessage = service.mapStreamToAccumulator(
                service.streamChatCompletion(chatCompletionRequest))
                .blockingLast()
                .getAccumulatedMessage();
        verify(mockApi, times(1)).createChatCompletionStream(chatCompletionRequest);
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

        when(mockApi.createChatCompletionStream(chatCompletionRequest2)).
            // fake SSE Response
                thenReturn(new FakeCall<>(ResponseBody.create(MediaType.parse("text/event-stream"),
                "data: {\"choices\": [{\"finish_reason\": \"stop\", \"message\": {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}}]}\n\n")));
        ChatMessage accumulatedMessage2 = service.mapStreamToAccumulator(service.streamChatCompletion(chatCompletionRequest2))
                .blockingLast()
                .getAccumulatedMessage();
        verify(mockApi, times(2)).createChatCompletionStream(chatCompletionRequest2);
        assertNull(accumulatedMessage2.getFunctionCall());
        assertNotNull(accumulatedMessage2.getContent());
    }

    @Test
    void streamChatCompletionWithDynamicFunctions() throws JsonProcessingException {
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

        ChatCompletionResult result = ChatCompletionResult.builder()
            .choices(Collections.singletonList(ChatCompletionChoice.builder()
                .finishReason("function_call")
                .message(ChatMessage.builder()
                    .role(ChatMessageRole.FUNCTION.value())
                    .functionCall(ChatFunctionCall.builder()
                        .name("get_weather")
                        .arguments(JsonNodeFactory.instance.textNode("{\"location\": \"Monterrey, Nuevo León\", \"unit\": \"CELSIUS\"}"))
                        .build())
                    .build())
                .build())).build();
        ObjectMapper om = new ObjectMapper();
        // fake SSE Response
        when(mockApi.createChatCompletionStream(chatCompletionRequest)).
            thenReturn(new FakeCall<>(ResponseBody.create(MediaType.parse("text/event-stream"),
                "data: " + om.writeValueAsString(result) + "\n\n")));
        ChatMessage accumulatedMessage = service.mapStreamToAccumulator(service.streamChatCompletion(chatCompletionRequest))
                .blockingLast()
                .getAccumulatedMessage();
        verify(mockApi, times(1)).createChatCompletionStream(chatCompletionRequest);
        assertNotNull(accumulatedMessage.getFunctionCall());
        assertEquals("get_weather", accumulatedMessage.getFunctionCall().getName());
        assertInstanceOf(ObjectNode.class, accumulatedMessage.getFunctionCall().getArguments());
        assertNotNull(accumulatedMessage.getFunctionCall().getArguments().get("location"));
        assertNotNull(accumulatedMessage.getFunctionCall().getArguments().get("unit"));
    }

    @Test
    void testJsonMode() {
        ChatResponseFormat json = ChatResponseFormat.JSON;
        assertEquals("json_object", json.getType());

        ChatCompletionRequest request = ChatCompletionRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(Collections.singletonList(ChatMessage.builder()
                .role(ChatMessageRole.USER.value())
                .content("What is the weather in Monterrey, Nuevo León?")
                .build()))
            .responseFormat(ChatResponseFormat.JSON)
            .build();

        when(mockApi.createChatCompletion(request)).thenReturn(just(ChatCompletionResult.builder().build()));
        ChatCompletionResult result = service.createChatCompletion(request);
        verify(mockApi, times(1)).createChatCompletion(request);
        assertNotNull(result);
    }
}
